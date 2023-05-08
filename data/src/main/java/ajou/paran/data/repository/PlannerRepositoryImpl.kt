package ajou.paran.data.repository

import ajou.paran.domain.model.BasePlanner
import ajou.paran.domain.repository.PlannerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlannerRepositoryImpl
@Inject
constructor(

) : PlannerRepository {
    override suspend fun insertPlanner(planner: BasePlanner) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlannerById(plannerId: Long) {
        TODO("Not yet implemented")
    }

    override fun findPlannerById(plannerId: Long): BasePlanner? {
        TODO("Not yet implemented")
    }

    override fun deleteAllPlansByPlannerId(plannerId: Long) {
        TODO("Not yet implemented")
    }

    override fun updatePlanner(planner: BasePlanner) {
        TODO("Not yet implemented")
    }

    override fun selectAllPlanner(): Flow<List<BasePlanner>> {
        TODO("Not yet implemented")
    }
}