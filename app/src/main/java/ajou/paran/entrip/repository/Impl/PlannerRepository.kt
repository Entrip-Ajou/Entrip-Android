package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.dto.PlannerUpdateRequest
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import kotlinx.coroutines.flow.Flow

interface PlannerRepository {

    suspend fun updatePlanner(plannerId: Long, plannerUpdateRequest: PlannerUpdateRequest) : BaseResult<Unit, Failure>
    fun getFlowPlanner(plannerId: Long): Flow<PlannerEntity>

    // -------------------------      <Home화면에서의 Logic>    -----------------------------------------

    // step 1. Planner 객체만 local에서 가져와서 홈 화면 구성하기
    suspend fun selectAllPlanner(): Flow<List<PlannerEntity>>

    // step 2. Planner 추가 버튼을 눌렀을 때
    suspend fun createPlanner(user: String) : BaseResult<PlannerEntity, Failure>

    // step 3. Planner 삭제 버튼을 눌렀을 때
    suspend fun deletePlanner(plannerId: Long) : BaseResult<Unit, Failure>

    // step 4. Home 화면에서 planner를 선택했을 때
    suspend fun findPlanner(plannerId : Long) : BaseResult<PlannerEntity, Failure>

    // step 5. 서버와 sync를 맞춤(PlanRepository -> PlannerRepository로 Migration)
    suspend fun syncRemoteDB(planner_id: Long) : Flow<BaseResult<Any, Failure>>
}