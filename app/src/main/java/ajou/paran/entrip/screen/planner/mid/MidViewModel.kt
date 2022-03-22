package ajou.paran.entrip.screen.planner.mid

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.repository.room.plan.repository.PlanRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MidViewModel @Inject constructor(
    private val planRepository: PlanRepository
) : ViewModel() {

    lateinit var date : String
    lateinit var plannerId : String

    companion object {
        private const val TAG = "[MidViewModel]"
    }

    init {
        loadPlan(date, plannerId)
    }

    fun loadPlan(date: String, plannerId : String) : Flow<List<PlanEntity>> = planRepository.selectPlan(date,plannerId)

    fun deletePlan(planEntity: PlanEntity){
        viewModelScope.launch(Dispatchers.IO) {
            planRepository.deletePlan(planEntity)
        }
    }

    fun updateQueryPlan(planEntity:PlanEntity){
        viewModelScope.launch(Dispatchers.IO) {
            planRepository.updatePlan(
                planEntity.todo,
                planEntity.rgb,
                planEntity.time,
                planEntity.location.toString(),
                planEntity.id)
        }
    }
}