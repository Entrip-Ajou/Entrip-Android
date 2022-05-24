package ajou.paran.entrip.screen.planner.main

import ajou.paran.entrip.repository.Impl.PlannerRepositoryImpl
import ajou.paran.entrip.repository.Impl.UserAddRepositoryImpl
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}

