package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.dto.PlanRequest
import ajou.paran.entrip.repository.network.dto.PlanUpdateRequest
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import kotlinx.coroutines.flow.Flow

interface PlanRepository {
    suspend fun insertPlan(planRequest: PlanRequest) : BaseResult<Int, Failure>
    fun selectPlan(planDate : String, plannerId : Long) : Flow<List<PlanEntity>>
    suspend fun deletePlan(plan_id : Long, planner_id : Long) : BaseResult<Int, Failure>
    suspend fun updatePlan(plan_id:Long, plan: PlanUpdateRequest) : BaseResult<Int, Failure>

    suspend fun createPlanner(user_id : String) : Long
    suspend fun syncRemoteDB(planner_id: Long) : Flow<BaseResult<Any, Failure>>
}