package ajou.paran.entrip.screen.home

import ajou.paran.entrip.model.InviteEntity
import ajou.paran.entrip.repository.Impl.PlannerRepositoryImpl
import ajou.paran.entrip.repository.Impl.UserAddRepositoryImpl
import ajou.paran.entrip.repository.network.dto.PushNotification
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeActivityViewModel
@Inject
constructor(
    private val plannerRepository: PlannerRepositoryImpl,
    private val userAddRepositoryImpl: UserAddRepositoryImpl
)
: ViewModel() {
    companion object{
        const val TAG = "[HomeActivityVM]"
    }

    private val _state = MutableStateFlow<ApiState>(ApiState.Init)
    val state : StateFlow<ApiState> get() = _state

    fun setLoading() {
        _state.value = ApiState.IsLoading(true)
    }

    fun hideLoading() {
        _state.value = ApiState.IsLoading(false)
    }

    fun selectAllInvite() = userAddRepositoryImpl.selectInvite()

    fun countInvite() = userAddRepositoryImpl.countInvite()

    fun acceptInvitation(inviteEntity : InviteEntity, user_id : String, notificationData: PushNotification){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            when(val res1 = userAddRepositoryImpl.addUserToPlanner(inviteEntity.planner_id, user_id)){
                is BaseResult.Success -> {
                    when(val res2 = plannerRepository.acceptInvitation(inviteEntity.planner_id)){
                        is BaseResult.Success -> {
                            when(val res3 = userAddRepositoryImpl.postNotification(notificationData, inviteEntity)){
                                is BaseResult.Success -> { _state.value = ApiState.Success(res3.data) }
                                is BaseResult.Error -> { _state.value = ApiState.Failure(res3.err.code) }
                            }
                        }
                        is BaseResult.Error -> { _state.value = ApiState.Failure(res2.err.code) }
                    }
                }
                is BaseResult.Error -> { _state.value = ApiState.Failure(res1.err.code) }
            }
            delay(500)
            hideLoading()
        }
    }

    fun rejectInvitation(inviteEntity: InviteEntity, notificationData: PushNotification){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            when(val res = userAddRepositoryImpl.postNotification(notificationData, inviteEntity)){
                is BaseResult.Success -> { _state.value = ApiState.Success(res.data) }
                is BaseResult.Error -> { _state.value = ApiState.Failure(res.err.code) }
            }
            delay(500)
            hideLoading()
        }
    }

}