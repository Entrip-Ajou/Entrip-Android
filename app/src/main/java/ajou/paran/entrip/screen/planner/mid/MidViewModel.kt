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

    companion object {
        private const val TAG = "[MidViewModel]"
    }

    init {
        loadPlan()
    }

    fun loadPlan() : Flow<List<PlanEntity>> = planRepository.selectPlan()

    fun deletePlan(planEntity: PlanEntity){
        viewModelScope.launch(Dispatchers.IO) {
            planRepository.deletePlan(planEntity)
        }
    }

    fun updatePlan(planEntity:PlanEntity){
        viewModelScope.launch(Dispatchers.IO) {
            planRepository.updatePlan(planEntity)
        }
    }
}