package ajou.paran.entrip.screen.planner.top.useradd

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityUseraddBinding
import ajou.paran.entrip.repository.network.dto.NotificationData
import ajou.paran.entrip.repository.network.dto.PushNotification
import ajou.paran.entrip.repository.network.dto.SharingFriend
import ajou.paran.entrip.repository.network.dto.UserInformation
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import ajou.paran.entrip.util.ApiState
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PlannerUserAddActivity: BaseActivity<ActivityUseraddBinding>(
    R.layout.activity_useradd
), View.OnClickListener {
    companion object{
        const val TAG = "[PlannerUserAddActivity]"
    }

    private val viewModel: PlannerUserAddActivityViewModel by viewModels()
    private var planner_id : Long = 1       // todo : PlannerActivity에게 intent로 받기
    private lateinit var userInformation : UserInformation

    override fun init(savedInstanceState: Bundle?) {
        binding.plannerActivityViewModel = viewModel
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

                binding.layoutSearchSuccess.visibility = VISIBLE
            }
            is Unit -> {

            }
        }
    }

    private fun handleError(code : Int){

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
                    viewModel.searchUser(binding.etUserSearch.text.toString())
                }

                binding.tvInvite.id -> {
                    val notification = PushNotification(
                        NotificationData(
                            title = "Entrip",
                            message = "님이 플래너 초대를 보냈습니다.",
                            owner = "sharedPreference",
                            owner_token = "에 저장한거 꺼내와야겠다",
                            planner_id = planner_id,
                            isInvite = true
                        ), userInformation.token
                    )
                    viewModel.postNotification(notification, userInformation)
                }


            }

        }
    }
}