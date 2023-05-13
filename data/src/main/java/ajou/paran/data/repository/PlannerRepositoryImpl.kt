package ajou.paran.data.repository

import ajou.paran.data.local.datasource.PlannerRoomDataSource
import ajou.paran.domain.model.BasePlanner
import ajou.paran.domain.repository.PlannerRepository
import javax.inject.Inject

class PlannerRepositoryImpl
@Inject
constructor(
    private val plannerRoomDataSource: PlannerRoomDataSource,
) : PlannerRepository {
    override suspend fun insertPlanner(planner: BasePlanner) = plannerRoomDataSource.insertPlanner(planner)

    override suspend fun deletePlannerById(plannerId: Long) = plannerRoomDataSource.deletePlannerById(plannerId)

    override suspend fun findPlannerById(plannerId: Long): BasePlanner? = plannerRoomDataSource.findPlannerById(plannerId)

    override suspend fun deleteAllPlansByPlannerId(plannerId: Long) = plannerRoomDataSource.deleteAllPlansByPlannerId(plannerId)

    override suspend fun updatePlanner(planner: BasePlanner) = plannerRoomDataSource.updatePlanner(planner)

    override suspend fun selectAllPlanner(): List<BasePlanner> = plannerRoomDataSource.selectAllPlanner()

}