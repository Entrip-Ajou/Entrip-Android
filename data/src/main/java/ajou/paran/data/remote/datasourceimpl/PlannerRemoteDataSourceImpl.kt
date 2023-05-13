package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.mapper.toModel
import ajou.paran.data.remote.api.PlannerAPI
import ajou.paran.data.remote.datasource.PlannerRemoteDataSource
import ajou.paran.data.remote.model.request.UpdatePlannerRequest
import ajou.paran.data.remote.model.response.*
import ajou.paran.data.utils.baseApiCall
import ajou.paran.domain.model.BaseCondition
import ajou.paran.domain.model.BaseId
import ajou.paran.domain.model.BasePlanner
import ajou.paran.domain.model.DefaultUser
import javax.inject.Inject

class PlannerRemoteDataSourceImpl
@Inject
constructor(
    private val plannerAPI: PlannerAPI,
) : PlannerRemoteDataSource {
    override suspend fun createPlannerByUserId(userId: String): BasePlanner = baseApiCall {
        plannerAPI.createPlannerByUserId(userId = userId).apply {
            when (statusCode) {
                else -> {

                }
            }
        }.data.toModel()
    }

    override suspend fun updatePlanner(
        plannerId: Long,
        request: UpdatePlannerRequest
    ): BasePlanner = baseApiCall {
        plannerAPI.updatePlanner(
            plannerId = plannerId,
            request = request,
        ).apply {
            when (statusCode) {
                else -> {

                }
            }
        }.data.toModel()
    }

    override suspend fun findPlannerById(plannerId: Long): BasePlanner = baseApiCall {
        plannerAPI.findPlannerById(plannerId = plannerId).apply {
            when (statusCode) {
                else -> {

                }
            }
        }.data.toModel()
    }

    override suspend fun findAllUsersByPlannerId(plannerId: Long): DefaultUser.PlannerUser {
        TODO("Not yet implemented")
    }

    override suspend fun findAllPlansByPlannerId(plannerId: Long): FindAllPlansByPlannerIdResponseList {
        TODO("Not yet implemented")
    }

    override suspend fun findAllPlansByPlannerIdWithDate(
        plannerId: Long,
        date: String
    ): FindAllPlansByPlannerIdWithDateResponseList {
        TODO("Not yet implemented")
    }

    override suspend fun findAllNoticesByPlannerId(plannerId: Long): FindAllNoticesByPlannerIdResponseList {
        TODO("Not yet implemented")
    }

    override suspend fun findAllVotesByPlannerId(plannerId: Long): FindAllVotesByPlannerIdResponseList {
        TODO("Not yet implemented")
    }

    override suspend fun addUsersToPlanner(plannerId: Long, userId: String): BaseId {
        TODO("Not yet implemented")
    }

    override suspend fun exitUserFromPlanner(plannerId: Long, userId: String): BaseCondition {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlannerById(plannerId: Long): BaseId {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlannerAndExitById(plannerId: Long, userId: String): BaseId {
        TODO("Not yet implemented")
    }


}