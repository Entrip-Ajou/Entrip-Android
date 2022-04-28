package ajou.paran.entrip.screen.planner.mid

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.repository.Impl.PlanRepositoryImpl
import ajou.paran.entrip.util.network.BaseResult
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MidViewModel @Inject constructor(
    private val planRepository: PlanRepositoryImpl
) : ViewModel() {
    companion object {
        private const val TAG = "[MidViewModel]"
    }

    private val _state = MutableStateFlow<PlanState>(PlanState.Init)
    val state: StateFlow<PlanState> get() = _state

    private lateinit var lastTimeStamp: String
    private lateinit var job: Job

    fun setLoading() {
        _state.value = PlanState.IsLoading(true)
    }

    fun hideLoading() {
        _state.value = PlanState.IsLoading(false)
    }

    private fun setUpdate(){
        _state.value = PlanState.IsUpdate(true)
    }

    private fun setNotUpdate(){
        _state.value = PlanState.IsUpdate(false)
    }

    fun syncRemoteDB(planner_id: Long) {
        job = viewModelScope.launch(Dispatchers.IO) {
            planRepository.syncRemoteDB(planner_id)
                .catch { e ->
                    Log.e(TAG, "Error message = " + e.message)
                }
                .collect { res ->
                    when (res) {
                        is BaseResult.Success -> {
                            lastTimeStamp = res.data as String
                        }
                        is BaseResult.Error -> {
                            Log.e(TAG,"Error Code : ${res.err.code}, Error Message : ${res.err.message}")
                        }
                    }
                }
        }
    }

    fun observeTimeStamp(planner_id: Long){
        runBlocking{
            job.join()
        }
        viewModelScope.launch{
            planRepository.latestTimeStamp(planner_id).collectLatest{
                if(lastTimeStamp != it) setUpdate()
                else setNotUpdate()
            }
        }
    }

    fun loadPlan(date: String, plannerId: Long): Flow<List<PlanEntity>> =
        planRepository.selectPlan(date, plannerId)

    fun deletePlan(plan_id: Long, planner_id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = planRepository.deletePlan(plan_id, planner_id)
            if(res is BaseResult.Error) Log.e(TAG,"Error Code : ${res.err.code}, Error Message : ${res.err.message}")
        }
    }
}

sealed class PlanState {
    object Init : PlanState()
    data class IsLoading(val isLoading: Boolean) : PlanState()
    data class IsUpdate(val isUpdate: Boolean) : PlanState()
}
