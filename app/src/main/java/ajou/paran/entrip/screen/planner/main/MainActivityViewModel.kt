package ajou.paran.entrip.screen.planner.main

import ajou.paran.entrip.model.InviteEntity
import ajou.paran.entrip.repository.Impl.PlannerRepositoryImpl
import ajou.paran.entrip.repository.Impl.UserAddRepositoryImpl
import ajou.paran.entrip.repository.network.dto.NotificationData
import ajou.paran.entrip.repository.network.dto.PushNotification
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.Api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject
constructor(
    private val plannerRepository: PlannerRepositoryImpl,
    private val userAddRepositoryImpl: UserAddRepositoryImpl
) : ViewModel() {

    private val _state = MutableStateFlow<ApiState>(ApiState.Init)
    val state : StateFlow<ApiState> get() = _state

    fun setLoading() {
        _state.value = ApiState.IsLoading(true)
    }

    fun hideLoading() {
        _state.value = ApiState.IsLoading(false)
    }

    suspend fun selectAllPlanner() = plannerRepository.selectAllPlanner()

    fun selectAllInvite() = userAddRepositoryImpl.selectInvite()

    fun countInvite() = userAddRepositoryImpl.countInvite()

    fun createPlanner(userId : String){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = plannerRepository.createPlanner(userId)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(res.data)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun deletePlanner(plannerId : Long){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = plannerRepository.deletePlanner(plannerId)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(Unit)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun selectPlanner(plannerId : Long){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = plannerRepository.findPlanner(plannerId)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(res.data)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun acceptInvitation(inviteEntity : InviteEntity, user_id : String, notificationData: PushNotification){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res1 = userAddRepositoryImpl.addUserToPlanner(inviteEntity.planner_id, user_id)
            when(res1){
                is BaseResult.Success -> {
                    val res2 = plannerRepository.acceptInvitation(inviteEntity.planner_id)
                    when(res2){
                        is BaseResult.Success -> {
                            val res3 = userAddRepositoryImpl.postNotification(notificationData, inviteEntity)
                            when(res3){
                                is BaseResult.Success -> _state.value = ApiState.Success(res3.data)
                                is BaseResult.Error -> _state.value = ApiState.Failure(res3.err.code)
                            }
                        }
                        is BaseResult.Error -> _state.value = ApiState.Failure(res2.err.code)
                    }
                }
                is BaseResult.Error -> _state.value = ApiState.Failure(res1.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun rejectInvitation(inviteEntity: InviteEntity, notificationData: PushNotification){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = userAddRepositoryImpl.postNotification(notificationData, inviteEntity)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(res.data)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }
}

