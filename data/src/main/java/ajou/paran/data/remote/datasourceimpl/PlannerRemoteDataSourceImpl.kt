package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.remote.api.PlannerAPI
import ajou.paran.data.remote.datasource.PlannerRemoteDataSource
import javax.inject.Inject

class PlannerRemoteDataSourceImpl
@Inject
constructor(
    private val plannerAPI: PlannerAPI,
) : PlannerRemoteDataSource {

    override suspend fun createPlannerByUserId(
        userId: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlanner(
        userId: String,
        plannerId: Long,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun fetchPlannerByPlannerId(
        plannerId: Long
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchPlansInPlannerByPlannerId(
        plannerId: Long
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlanner(
        plannerId: Long,
        title: String,
        startDate: String,
        endDate: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun findPlanByPlannerIdWithDate(
        plannerId: Long,
        date: String,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun isExistPlannerById(
        plannerId: Long
    ) {
        TODO("Not yet implemented")
    }

}