package ajou.paran.data.local.datasource

import ajou.paran.domain.model.BasePlan
import ajou.paran.domain.model.BasePlanner

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

    suspend fun selectPlanByIdWithDate(
        planDate: String,
        plannerId: Long,
    ): List<BasePlan>

    suspend fun insertAllPlan(
        planList: List<BasePlan>,
    )

    suspend fun findPlannerById(
        plannerId: Long,
    ): BasePlanner?

    suspend fun findFlowPlannerById(
        plannerId: Long,
    ): BasePlanner

    suspend fun deleteAllPlansByPlannerId(
        plannerId: Long,
    )

    suspend fun updatePlanner(
        planner: BasePlanner,
    )

    suspend fun updatePlan(
        plan: BasePlan,
    )

    suspend fun selectAllPlanner(): List<BasePlanner>

    suspend fun deletePlanByPlannerIdWithDate(
        plannerId: Long,
        date: String,
    )

    suspend fun updateIsExistCommentsWithPlanId(
        isExistComments: Boolean,
        planId: Long
    )
}