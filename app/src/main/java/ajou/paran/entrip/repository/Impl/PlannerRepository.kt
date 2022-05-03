package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import kotlinx.coroutines.flow.Flow

interface PlannerRepository {
    /**
     * @fun PlannerRemoteSource에 존재하는 함수
     * @순서 PlannerRemoteSource에 존재하는 순서
     * **/

    suspend fun updatePlanner(plannerId: Long, plannerEntity: PlannerEntity) : BaseResult<Int, Failure>
    suspend fun insertPlanner(plannerEntity: PlannerEntity) : BaseResult<Int, Failure>
    suspend fun selectAllPlan(plannerId: Long) : List<PlanEntity>
    fun deleteAllPlan(plannerId: Long)
    fun getFlowPlanner(plannerId: Long): Flow<PlannerEntity>
    fun findDBPlanner(plannerId: Long): PlannerEntity?

    // -------------------------      <Home화면에서의 Logic>    -----------------------------------------

    // step 1. Planner 객체만 local에서 가져와서 홈 화면 구성하기
    suspend fun selectAllPlanner(): Flow<List<PlannerEntity>>

    // step 2. Planner 추가 버튼을 눌렀을 때
    suspend fun createPlanner(userId: String) : BaseResult<PlannerEntity, Failure>

    // step 3. Planner 삭제 버튼을 눌렀을 때
    suspend fun deletePlanner(plannerId: Long) : BaseResult<Unit, Failure>

    // step 4. Home 화면에서 planner를 선택했을 때
    suspend fun findPlanner(plannerId : Long) : BaseResult<PlannerEntity, Failure>
}