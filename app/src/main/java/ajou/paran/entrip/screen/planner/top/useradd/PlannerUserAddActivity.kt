package ajou.paran.entrip.screen.planner.top.useradd

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseActivity
import ajou.paran.entrip.databinding.ActivityUseraddBinding
import ajou.paran.entrip.repository.network.dto.SharingFriend
import ajou.paran.entrip.util.ApiState
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
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
        if(data is List<*>){
            binding.rvSharingPlanner.adapter?.let{ a ->
                if(a is SharingAdapter)
                    a.update(data as List<SharingFriend>)
            }
        }
    }

    private fun handleError(code : Int){

    }

    override fun onClick(view: View?) {
        view?.let {
            when(it.id){
                /*
                binding.userAddActBtnClose.id -> {
                    onBackPressed()
                }
                 */
            }

        }
    }
}