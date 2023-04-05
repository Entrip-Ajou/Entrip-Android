package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.remote.api.PlanAPI
import ajou.paran.data.remote.datasource.PlanRemoteDataSource
import javax.inject.Inject

class PlanRemoteDataSourceImpl
@Inject
constructor(
    private val planAPI: PlanAPI,
) : PlanRemoteDataSource {

    override suspend fun addPlan(
        plannerId: Long,
        date: String,
        todo: String,
        time: Int,
        location: String?
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlanById(
        planId: Long,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlanById(
        planId: Long,
        date: String,
        todo: String,
        time: Int,
        location: String?
    ) {
        TODO("Not yet implemented")
    }

}