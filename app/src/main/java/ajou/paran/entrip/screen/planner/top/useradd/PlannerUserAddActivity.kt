package ajou.paran.entrip.screen.planner.top.useradd

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityUseraddBinding
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.model.WaitEntity
import ajou.paran.entrip.repository.network.dto.NotificationData
import ajou.paran.entrip.repository.network.dto.PushNotification
import ajou.paran.entrip.repository.network.dto.SharingFriend
import ajou.paran.entrip.repository.network.dto.UserInformation
import ajou.paran.entrip.screen.planner.main.MainActivity
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import ajou.paran.entrip.util.ApiState
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class PlannerUserAddActivity : BaseActivity<ActivityUseraddBinding>(
    R.layout.activity_useradd
), View.OnClickListener {

    companion object {
        private const val TAG = "[UserAddActivity]"
    }

    private val viewModel: PlannerUserAddActivityViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var planner_id: Long = -1L
    private lateinit var planner_title: String
    private lateinit var selectedPlanner: PlannerEntity
    private lateinit var userInformation: UserInformation

    override fun init(savedInstanceState: Bundle?) {
        binding.plannerActivityViewModel = viewModel
        selectedPlanner = intent.getParcelableExtra("PlannerEntity")!!
        planner_id = selectedPlanner.planner_id
        planner_title = selectedPlanner.title

        observeState()
        setUpSharingRecyclerView()
        setUpWaitingRecyclerView()
    }

    private fun setUpSharingRecyclerView() {
        binding.rvSharingPlanner.apply{
            adapter = SharingAdapter(mutableListOf(), sharedPreferences.getString("user_id", null).toString())
        }
        viewModel.findAllUserWithPlannerId(planner_id)
    }

    private fun setUpWaitingRecyclerView() {
        val waitAdapter = WaitingAdapter()
        binding.rvWaitingPlanner.adapter = waitAdapter

        lifecycle.coroutineScope.launch{
            viewModel.selectWait(planner_id)
                .onStart{ viewModel.setLoading() }
                .catch{e ->
                    viewModel.hideLoading()
                }
                .collect{
                    viewModel.hideLoading()
                    waitAdapter.submitList(it.toList())
                }
        }
    }

    private fun observeState() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: ApiState) {
        when (state) {
            is ApiState.Init -> Unit
            is ApiState.IsLoading -> handleLoading(state.isLoading)
            is ApiState.Success -> handleSuccess(state.data)
            is ApiState.Failure -> handleError(state.code)
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.sharingProgress.visibility = View.VISIBLE
        } else {
            binding.sharingProgress.visibility = View.GONE
        }
    }

    /**
     *   Success일 때 data type
     *   List<SharingFriend> -> sharingRecyclerView
     *   WaitEntity -> waitingRecyclerView
     *
     */
    private fun handleSuccess(data: Any) {
        when (data) {
            is List<*> -> {
                binding.rvSharingPlanner.adapter?.let { a ->
                    if (a is SharingAdapter)
                        a.update(data as List<SharingFriend>)
                }
            }

            is UserInformation -> {
                Glide.with(this)
                    .load(data.photoUrl)
                    .into(binding.imageProfile)
                binding.tvUserOrNickname.text = data.nickname

                userInformation = UserInformation(
                    nickname = data.nickname,
                    token = data.token,
                    photoUrl = data.photoUrl
                )

                binding.layoutSearchSuccess.visibility = View.VISIBLE
                binding.tvSearchFailure.visibility = View.INVISIBLE

                lifecycle.coroutineScope.launch(Dispatchers.IO){
                    val isExist = viewModel.isExistNickname(data.nickname)
                    withContext(Dispatchers.Main){
                        if(isExist || sharedPreferences.getString("nickname", null) == data.nickname){
                            binding.tvInvite.visibility = View.INVISIBLE
                        }else{
                            binding.tvInvite.visibility = View.VISIBLE
                            binding.tvInvite.isClickable = true
                            binding.tvInvite.setTextColor(Color.parseColor("#0d82eb"))
                        }
                    }
                }
            }
            is Unit -> {}
        }
    }

    private fun handleError(code: Int) {
        when (code) {
            0 -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("네트워크를 확인해주세요")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, which -> })
                builder.show()
            }

            404 -> {
                Toast.makeText(this, "검색 결과가 없습니다", Toast.LENGTH_LONG).show()
                binding.layoutSearchSuccess.visibility = View.INVISIBLE
                binding.tvSearchFailure.visibility = View.VISIBLE
            }

            500 -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("다른 사용자로 의해 삭제되었습니다.")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, which ->
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        })
                builder.show()
            }

            -1 -> {
                Log.e(TAG, "최상위 Exception class에서 예외 발생 -> 코드 로직 오류")
            }

            else -> {
                Log.e(TAG, "${code} Error handleError()에 추가 및 trouble shooting하기")
            }
        }
    }

    override fun onClick(view: View?) {
        view?.let {
            when (it.id) {
                binding.btnBackToPlanner.id -> {
                    val intent = Intent(this, PlannerActivity::class.java)
                    intent.putExtra("PlannerEntity", selectedPlanner)
                    startActivity(intent)
                    finish()
                }

                binding.imgUserSearch.id -> {
                    val user = binding.etUserSearch.text.toString()
                    if (user.isNotBlank()) viewModel.searchUser(user)
                }

                binding.tvInvite.id -> {
                    val notification = PushNotification(
                        NotificationData(
                            title = "Entrip",
                            message = "${
                                sharedPreferences.getString("nickname", null).toString()
                            }님이 플래너 초대를 보냈습니다.",
                            owner = sharedPreferences.getString("nickname", null).toString(),
                            owner_token = sharedPreferences.getString("token", null).toString(),
                            photo_url = sharedPreferences.getString("photo_url", null).toString(),
                            planner_id = planner_id,
                            planner_title = planner_title,
                            isInvite = true
                        ), userInformation.token
                    )
                    viewModel.postNotification(notification, userInformation)
                    binding.tvInvite.isClickable = false
                    binding.tvInvite.setTextColor(Color.parseColor("#9e9e9e"))
                }


            }

        }
    }
}