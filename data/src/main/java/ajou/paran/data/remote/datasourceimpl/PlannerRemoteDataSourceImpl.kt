package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.remote.api.PlannerAPI
import ajou.paran.data.remote.datasource.PlannerRemoteDataSource
import ajou.paran.data.utils.baseApiCall
import javax.inject.Inject

class PlannerRemoteDataSourceImpl
@Inject
constructor(
    private val plannerAPI: PlannerAPI,
) : PlannerRemoteDataSource {

    override suspend fun createPlannerByUserId(
        userId: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlanner(
        userId: String,
        plannerId: Long,
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun fetchPlannerByPlannerId(
        plannerId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun fetchPlansInPlannerByPlannerId(
        plannerId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlanner(
        plannerId: Long,
        title: String,
        startDate: String,
        endDate: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun findPlanByPlannerIdWithDate(
        plannerId: Long,
        date: String,
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun isExistPlannerById(
        plannerId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

}