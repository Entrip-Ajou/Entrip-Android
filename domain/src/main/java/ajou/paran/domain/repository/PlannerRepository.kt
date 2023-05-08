package ajou.paran.domain.repository

import ajou.paran.domain.model.BasePlanner
import kotlinx.coroutines.flow.Flow

interface PlannerRepository {

    suspend fun insertPlanner(
        planner: BasePlanner
    )

    suspend fun deletePlannerById(
        plannerId: Long,
    )

    fun findPlannerById(
        plannerId: Long,
    ): BasePlanner?

    fun deleteAllPlansByPlannerId(
        plannerId: Long,
    )

    fun updatePlanner(
        planner: BasePlanner,
    )

    fun selectAllPlanner(): List<BasePlanner>

}