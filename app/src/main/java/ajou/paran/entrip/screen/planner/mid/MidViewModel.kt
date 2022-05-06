package ajou.paran.entrip.screen.planner.mid

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.repository.Impl.PlanRepositoryImpl
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MidViewModel @Inject constructor(
    private val planRepository: PlanRepositoryImpl
) : ViewModel() {
    companion object {
        private const val TAG = "[MidViewModel]"
    }

    private val _state = MutableStateFlow<ApiState>(ApiState.Init)
    val state: StateFlow<ApiState> get() = _state

    fun setLoading() {
        _state.value = ApiState.IsLoading(true)
    }

    fun hideLoading() {
        _state.value = ApiState.IsLoading(false)
    }

    fun loadPlan(date: String, plannerId: Long): Flow<List<PlanEntity>> =
        planRepository.selectPlan(date, plannerId)

    fun deletePlan(plan_id: Long, planner_id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = planRepository.deletePlan(plan_id, planner_id)
            hideLoading()
            if(res is BaseResult.Success) _state.value = ApiState.Success(Unit)
            else _state.value = ApiState.Failure((res as BaseResult.Error).err.code)
        }
    }
}
