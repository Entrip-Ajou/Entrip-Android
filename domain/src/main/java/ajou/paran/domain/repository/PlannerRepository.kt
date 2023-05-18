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

    suspend fun deletePlanByPlannerIdWithDate(
        plannerId: Long,
        date: String,
    )

    suspend fun createPlannerByUserId(
        userId: String
    )

    suspend fun deletePlanner(
        userId: String,
        plannerId: Long,
    )

    suspend fun fetchPlannerByPlannerId(
        plannerId: Long,
    )

    suspend fun fetchPlansInPlannerByPlannerId(
        plannerId: Long
    )

    suspend fun updateRemotePlanner(
        plannerId: Long,
        title: String,
        startDate: String,
        endDate: String,
    ): BasePlanner

    suspend fun findPlanByPlannerIdWithDate(
        plannerId: Long,
        date: String
    )

    suspend fun isExistPlannerById(
        plannerId: Long
    )

}