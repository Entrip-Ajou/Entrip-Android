package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerDate
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import kotlinx.coroutines.flow.Flow

interface PlannerRepository {
    /**
     * @fun PlannerRemoteSource에 존재하는 함수
     * @순서 PlannerRemoteSource에 존재하는 순서
     * **/
    suspend fun createPlanner(userId: String) : Long
    suspend fun updatePlanner(plannerId: Long, plannerEntity: PlannerEntity) : BaseResult<Int, Failure>
    suspend fun findPlanner(plannerId : Long) : PlannerEntity
    suspend fun deletePlanner(plannerId: Long) : Long

    suspend fun insertPlanner(plannerEntity: PlannerEntity) : BaseResult<Int, Failure>
    suspend fun selectAllPlan(plannerId: Long) : List<PlanEntity>
    fun deleteAllPlan(plannerId: Long)
    fun getFlowPlanner(plannerId: Long): Flow<PlannerEntity>
}