package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.dto.PlannerUpdateRequest
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject

interface PlannerRepository {

    suspend fun updatePlanner(plannerId: Long, plannerUpdateRequest: PlannerUpdateRequest) : BaseResult<Unit, Failure>
    fun getFlowPlanner(plannerId: Long): Flow<PlannerEntity>
    suspend fun selectAllPlanner(): Flow<List<PlannerEntity>>
    suspend fun createPlanner(user: String) : BaseResult<PlannerEntity, Failure>
    suspend fun deletePlanner(user_id : String, plannerId: Long) : BaseResult<Unit, Failure>
    suspend fun findPlanner(plannerId : Long) : BaseResult<PlannerEntity, Failure>
    suspend fun syncRemoteDB(planner_id: Long) : Flow<BaseResult<Any, Failure>>
    suspend fun isExist(planner_id : Long) : BaseResult<Boolean, Failure>
}