package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface PlanRepository {
    suspend fun insertPlan(planEntity: PlanEntity)
    fun selectPlan(planDate : String, plannerId : Long) : List<PlanEntity>
    fun deletePlan(planEntity: PlanEntity)
    fun updatePlan(todo:String, rgb:Int, time : Int, location : String, id : Long)

    suspend fun createPlanner() : Long
    suspend fun syncRemoteDB(plannerEntity: PlannerEntity) : Flow<BaseResult<Any, Failure>>
}