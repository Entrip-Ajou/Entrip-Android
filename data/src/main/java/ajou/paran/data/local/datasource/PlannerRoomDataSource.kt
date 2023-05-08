package ajou.paran.data.local.datasource

import ajou.paran.domain.model.BasePlan
import ajou.paran.domain.model.BasePlanner
import kotlinx.coroutines.flow.Flow

interface PlannerRoomDataSource {
    suspend fun insertPlan(
        plan: BasePlan,
    )

    suspend fun insertPlanner(
        planner: BasePlanner
    )

    suspend fun deletePlanById(
        planId: Long,
    )

    suspend fun deletePlannerById(
        plannerId: Long,
    )

    fun selectPlanByIdWithDate(
        planDate: String,
        plannerId: Long,
    ): Flow<List<BasePlan>>

    fun insertAllPlan(
        planList: List<BasePlan>,
    )

    fun findPlannerById(
        plannerId: Long,
    ): BasePlanner?

    fun findFlowPlannerById(
        plannerId: Long,
    ) : Flow<BasePlanner>

    fun deleteAllPlansByPlannerId(
        plannerId: Long,
    )

    fun updatePlanner(
        planner: BasePlanner,
    )

    suspend fun updatePlan(
        plan: BasePlan,
    )

    fun selectAllPlanner(): Flow<List<BasePlanner>>

    fun deletePlanByPlannerIdWithDate(
        plannerId: Long,
        date: String,
    )

    fun updateIsExistCommentsWithPlanId(
        isExistComments: Boolean,
        planId: Long
    )
}