package ajou.paran.entrip.screen.planner.main

import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.Impl.PlannerRepository
import ajou.paran.entrip.screen.planner.mid.PlanState
import ajou.paran.entrip.screen.planner.top.PlannerActivity
import ajou.paran.entrip.util.network.BaseResult
import android.app.AlertDialog
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject
constructor(private val plannerRepository: PlannerRepository)
    : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Init)
    val state : StateFlow<HomeState> get() = _state

    fun setLoading() {
        _state.value = HomeState.IsLoading(true)
    }

    fun hideLoading() {
        _state.value = HomeState.IsLoading(false)
    }

    suspend fun selectAllPlanner() = plannerRepository.selectAllPlanner()

    fun createPlanner(userId : String){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = plannerRepository.createPlanner(userId)
            hideLoading()
            when(res){
                is BaseResult.Success -> _state.value = HomeState.Success(res.data)
                is BaseResult.Error -> _state.value = HomeState.Failure
            }
        }
    }

    fun deletePlanner(plannerId : Long){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = plannerRepository.deletePlanner(plannerId)
            hideLoading()
            when(res){
                is BaseResult.Success -> _state.value = HomeState.Success(Unit)
                is BaseResult.Error -> _state.value = HomeState.Failure
            }
        }
    }

    fun selectPlanner(plannerId : Long){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = plannerRepository.findPlanner(plannerId)
            hideLoading()
            when(res){
                is BaseResult.Success -> _state.value = HomeState.Success(res.data)

                is BaseResult.Error -> if(res.err.code == 1){
                    _state.value = HomeState.NoExist
                }else{
                    _state.value = HomeState.Failure
                }
            }
        }
    }
}

sealed class HomeState {
    object Init : HomeState()
    data class IsLoading(val isLoading : Boolean) : HomeState()
    data class Success(val data : Any) : HomeState()
    object Failure : HomeState()
    object NoExist : HomeState()
}