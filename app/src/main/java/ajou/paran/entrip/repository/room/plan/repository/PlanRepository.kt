package ajou.paran.entrip.repository.room.plan.repository

import ajou.paran.entrip.model.PlanEntity
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface PlanRepository {
    suspend fun insertPlan(planEntity: PlanEntity)
    fun selectPlan(planDate : String, plannerId : String) : Flow<List<PlanEntity>>
    fun deletePlan(planEntity: PlanEntity)
    fun updatePlan(todo:String, rgb:Int, time : Int, location : String, id : Long)
}