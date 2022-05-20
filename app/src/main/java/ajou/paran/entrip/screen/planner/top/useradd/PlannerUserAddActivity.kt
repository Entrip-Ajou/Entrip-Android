package ajou.paran.entrip.screen.planner.top.useradd

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityUseraddBinding
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
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlannerUserAddActivity: BaseActivity<ActivityUseraddBinding>(
    R.layout.activity_useradd
), View.OnClickListener {

    companion object{
        private const val TAG = "[UserAddActivity]"
    }

    private val viewModel: PlannerUserAddActivityViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences : SharedPreferences

    private var planner_id : Long = -1L
    private lateinit var planner_title : String
    private lateinit var userInformation : UserInformation

    override fun init(savedInstanceState: Bundle?) {
        binding.plannerActivityViewModel = viewModel
        planner_id = intent.getLongExtra("planner_id",-1)
        planner_title = intent.getStringExtra("planner_title").toString()
        observeState()
        setUpSharingRecyclerView()
        viewModel.findAllUserWithPlannerId(planner_id)
        setUpWaitingRecyclerView()
    }

    private fun setUpSharingRecyclerView(){
        binding.rvSharingPlanner.apply{
            adapter = SharingAdapter(mutableListOf())
        }
    }

    private fun setUpWaitingRecyclerView(){
        binding.rvWaitingPlanner.apply{
            adapter = WaitingAdapter(mutableListOf())
        }
    }

    private fun observeState(){
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach{ handleState(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state : ApiState){
        when(state){
            is ApiState.Init -> Unit
            is ApiState.IsLoading -> handleLoading(state.isLoading)
            is ApiState.Success -> handleSuccess(state.data)
            is ApiState.Failure -> handleError(state.code)
        }
    }

    private fun handleLoading(isLoading : Boolean){
        if(isLoading){
            binding.sharingProgress.visibility = View.VISIBLE
        }else{
            binding.sharingProgress.visibility = View.GONE
        }
    }

    /**
     *   Success일 때 data type
     *   List<SharingFriend> -> sharingRecyclerView
     *   WaitEntity -> waitingRecyclerView
     *
     */
    private fun handleSuccess(data : Any){
        when (data) {
            is List<*> -> {
                binding.rvSharingPlanner.adapter?.let{ a ->
                    if(a is SharingAdapter)
                        a.update(data as List<SharingFriend>)
                }
            }
            is UserInformation -> {
                Glide.with(this)
                    .load(data.photoUrl)
                    .into(binding.imageProfile)
                binding.tvUserOrNickname.text = data.nickname

                userInformation.nickname = data.nickname
                userInformation.token = data.token
                userInformation.photoUrl = data.photoUrl

                binding.layoutSearchSuccess.visibility = View.VISIBLE
                binding.tvSearchFailure.visibility = View.GONE
            }
            is Flow<*> -> {
                binding.rvWaitingPlanner.adapter?.let{ a ->
                    if(a is WaitingAdapter)
                        CoroutineScope(Dispatchers.IO).launch{
                            a.update(data as List<WaitEntity>)
                        }
                }
            }
        }
    }

    private fun handleError(code : Int){
        when(code){
            0 -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("네트워크를 확인해주세요")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener{ dialog, which -> })
                builder.show()
            }

            404 -> {
                Toast.makeText(this, "검색 결과가 없습니다", Toast.LENGTH_LONG).show()
                binding.layoutSearchSuccess.visibility = View.GONE
                binding.tvSearchFailure.visibility = View.VISIBLE
            }

            500 -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("다른 사용자로 의해 삭제되었습니다.")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener{ dialog, which ->
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
            when(it.id){
                binding.btnBackToPlanner.id -> {
                    val intent = Intent(this, PlannerActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                binding.imgUserSearch.id -> {
                    val user = binding.etUserSearch.text.toString()
                    if(user.isNotBlank()) viewModel.searchUser(user)
                }

                binding.tvInvite.id -> {
                    val notification = PushNotification(
                        NotificationData(
                            title = "Entrip",
                            message = "${sharedPreferences.getString("nickname", "default").toString()}님이 플래너 초대를 보냈습니다.",
                            owner = sharedPreferences.getString("nickname", "default").toString(),
                            owner_token = sharedPreferences.getString("token", "default").toString(),
                            photo_url = sharedPreferences.getString("photo_url", "default").toString(),
                            planner_id = planner_id,
                            planner_title = planner_title,
                            isInvite = true
                        ), userInformation.token
                    )
                    viewModel.postNotification(notification, userInformation)
                }


            }

        }
    }
}