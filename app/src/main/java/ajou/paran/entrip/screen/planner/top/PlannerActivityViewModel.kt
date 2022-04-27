package ajou.paran.entrip.screen.planner.top

import ajou.paran.entrip.model.PlannerDate
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.Impl.PlannerRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @ClassName: PlannerActivityViewModel
 * @innerFunc: setPlannerDateItem(List<PlannerDate>), getPlannerDateItem()
 * @private: _plannerDateItemList: MutableLiveData<List<PlannerDate>>
 * **/
@HiltViewModel
class PlannerActivityViewModel
@Inject
constructor(
    private val plannerRepository: PlannerRepository
) : ViewModel() {
    companion object{
        private const val TAG = "[PlannerActViewModel]"
    }

    private var plannerId: Long = 0L

    fun getFlowPlanner(): Flow<PlannerEntity> = plannerRepository.getFlowPlanner(plannerId)

    fun plannerDataDelete(){
        TODO("Case: Delete Planner Data")
    }

    fun plannerAdd(userId: String): Flow<Long> = flow {
        val planenrId = plannerRepository.createPlanner(userId)
        if(planenrId != -1L){
            plannerRepository.insertPlanner(plannerRepository.findPlanner(plannerId))
            emit(planenrId)
        } else {
            emit(plannerId)
        }
    }

    fun setting(planner_id: Long) = CoroutineScope(Dispatchers.IO).launch {
        plannerId = planner_id
        if (plannerId > 0L) {
            plannerRepository.insertPlanner(plannerRepository.findPlanner(plannerId))
        } else {
            TODO("planner가 존재하지 않는 경우")
        }
    }

    fun plannerChange(list: List<PlannerDate>) = CoroutineScope(Dispatchers.IO).launch {
        val planner = plannerRepository.findPlanner(plannerId)
        plannerRepository.updatePlanner(plannerId, planner.let { t ->
            PlannerEntity(
                planner_id = t.planner_id,
                title = t.title,
                start_date = list.first().date,
                end_date = list.last().date,
                timeStamp = t.timeStamp
            )
        })
    }

    fun plannerChange(title: String) = CoroutineScope(Dispatchers.IO).launch {
        val planner = plannerRepository.findPlanner(plannerId)
        plannerRepository.updatePlanner(plannerId, planner.let { t ->
            PlannerEntity(
                planner_id = t.planner_id,
                title = title,
                start_date = t.start_date,
                end_date = t.end_date,
                timeStamp = t.timeStamp
            )
        })
    }
}