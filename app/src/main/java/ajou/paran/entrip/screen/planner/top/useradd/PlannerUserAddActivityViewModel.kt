package ajou.paran.entrip.screen.planner.top.useradd

import ajou.paran.entrip.repository.Impl.PlannerRepositoryImpl
import ajou.paran.entrip.repository.Impl.UserAddRepositoryImpl
import ajou.paran.entrip.repository.network.dto.PushNotification
import ajou.paran.entrip.repository.network.dto.UserInformation
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import android.util.Log
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
class PlannerUserAddActivityViewModel
@Inject
constructor(private val userAddRepository: UserAddRepositoryImpl) : ViewModel() {
    companion object {
        const val TAG = "[UserAdd_VM]"
    }

    private val _state = MutableStateFlow<ApiState>(ApiState.Init)
    val state : StateFlow<ApiState> get() = _state

    fun setLoading() {
        _state.value = ApiState.IsLoading(true)
    }

    fun hideLoading() {
        _state.value = ApiState.IsLoading(false)
    }

    fun findAllUserWithPlannerId(planner_id : Long){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = userAddRepository.findAllUsersWithPlannerId(planner_id)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(res.data)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun searchUser(user_id_or_nickname : String){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = userAddRepository.searchUser(user_id_or_nickname)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(res.data)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun postNotification(notification: PushNotification, user : UserInformation){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = userAddRepository.postNotification(notification, user)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(Unit)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }
}