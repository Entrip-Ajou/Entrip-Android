package ajou.paran.domain.repository

import ajou.paran.domain.model.BasePlanner

interface PlannerRepository {

    suspend fun insertPlanner(
        planner: BasePlanner
    )

    suspend fun deletePlannerById(
        plannerId: Long,
    )

    suspend fun findPlannerById(
        plannerId: Long,
    ): BasePlanner?

    suspend fun deleteAllPlansByPlannerId(
        plannerId: Long,
    )

    suspend fun updatePlanner(
        planner: BasePlanner,
    )

    suspend fun selectAllPlanner(): List<BasePlanner>

}