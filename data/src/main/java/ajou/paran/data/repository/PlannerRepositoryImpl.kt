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

    override suspend fun deletePlanByPlannerIdWithDate(plannerId: Long, date: String) {
        TODO("Not yet implemented")
    }

    override suspend fun createPlannerByUserId(userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlanner(userId: String, plannerId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchPlannerByPlannerId(plannerId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchPlansInPlannerByPlannerId(plannerId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun updateRemotePlanner(
        plannerId: Long,
        title: String,
        startDate: String,
        endDate: String,
        timeStamp: String,
        commentTimeStamp: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun findPlanByPlannerIdWithDate(plannerId: Long, date: String) {
        TODO("Not yet implemented")
    }

    override suspend fun isExistPlannerById(plannerId: Long) {
        TODO("Not yet implemented")
    }

}