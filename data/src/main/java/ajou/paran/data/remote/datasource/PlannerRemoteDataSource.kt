package ajou.paran.data.remote.datasource

import ajou.paran.data.remote.model.request.UpdatePlannerRequest
import ajou.paran.data.remote.model.response.*
import ajou.paran.domain.model.BaseCondition
import ajou.paran.domain.model.BaseId
import ajou.paran.domain.model.BasePlanner
import ajou.paran.domain.model.DefaultUser

interface PlannerRemoteDataSource {

    suspend fun createPlannerByUserId(
        userId: String
    ): BasePlanner

    suspend fun updatePlanner(
        plannerId: Long,
        request: UpdatePlannerRequest
    ): BasePlanner

    suspend fun findPlannerById(
        plannerId: Long
    ): BasePlanner

    suspend fun findAllUsersByPlannerId(
        plannerId: Long
    ): DefaultUser.PlannerUser

    suspend fun findAllPlansByPlannerId(
        plannerId: Long
    ): FindAllPlansByPlannerIdResponseList

    suspend fun findAllPlansByPlannerIdWithDate(
        plannerId: Long,
        date: String,
    ): FindAllPlansByPlannerIdWithDateResponseList

    suspend fun findAllNoticesByPlannerId(
        plannerId: Long,
    ): FindAllNoticesByPlannerIdResponseList

    suspend fun findAllVotesByPlannerId(
        plannerId: Long,
    ): FindAllVotesByPlannerIdResponseList

    suspend fun addUsersToPlanner(
        plannerId: Long,
        userId: String,
    ): BaseId

    suspend fun exitUserFromPlanner(
        plannerId: Long,
        userId: String,
    ): BaseCondition

    suspend fun deletePlannerById(
        plannerId: Long,
    ): BaseId

    suspend fun deletePlannerAndExitById(
        plannerId: Long,
        userId: String,
    ): BaseId

}