package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.mapper.toModel
import ajou.paran.data.remote.api.PlanAPI
import ajou.paran.data.remote.datasource.PlanRemoteDataSource
import ajou.paran.data.remote.model.request.AddPlanRequest
import ajou.paran.data.utils.baseApiCall
import ajou.paran.domain.model.BasePlan
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
        location: String?,
        rgb: Long,
    ): BasePlan = baseApiCall {
        planAPI.addPlan(
            request = AddPlanRequest(
                date = date,
                todo = todo,
                time = time,
                location = location,
                rgb = rgb,
                plannerId = plannerId
            )
        ).data.toModel()
    }

    override suspend fun deletePlanById(
        planId: Long,
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlanById(
        planId: Long,
        date: String,
        todo: String,
        time: Int,
        location: String?
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

}