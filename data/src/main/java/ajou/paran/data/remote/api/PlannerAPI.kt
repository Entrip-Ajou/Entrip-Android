package ajou.paran.data.remote.api

import ajou.paran.data.remote.model.request.UpdatePlannerRequest
import ajou.paran.data.remote.model.response.*
import retrofit2.http.*

interface PlannerAPI {

    @POST("api/v1/planners/{user_id}")
    suspend fun createPlannerByUserId(
        @Path("user_id") userId: String
    ): BaseResponse<CreatePlannerByUserIdResponse>

    @PUT("api/v1/planners/{planner_id}")
    suspend fun updatePlanner(
        @Path("planner_id") plannerId: Long,
        @Body request: UpdatePlannerRequest
    ): BaseResponse<UpdatePlannerResponse>

    @GET("api/v1/planners/{planner_id}")
    suspend fun findPlannerById(
        @Path("planner_id") plannerId: Long
    ): BaseResponse<FindPlannerByIdResponse>

    @GET("/api/v1/planners/{planner_id}/getAllUser")
    suspend fun findAllUsersByPlannerId(
        @Path("planner_id") plannerId: Long
    ): BaseResponse<FindAllUsersByPlannerIdResponseList>

    @GET("api/v1/planners/{planner_id}/all")
    suspend fun findAllPlansByPlannerId(
        @Path("planner_id") plannerId: Long
    ): BaseResponse<FindAllPlansByPlannerIdResponseList>

    @GET("/api/v1/planners/{planner_id}/{date}/find")
    suspend fun findAllPlansByPlannerIdWithDate(
        @Path("planner_id") plannerId: Long,
        @Path("date") date: String,
    ): BaseResponse<FindAllPlansByPlannerIdWithDateResponseList>

    @GET("/api/v1/planners/{planner_id}/allNotices")
    suspend fun findAllNoticesByPlannerId(
        @Path("planner_id") plannerId: Long,
    ): BaseResponse<FindAllNoticesByPlannerIdResponseList>

    @GET("/api/v1/planners/1/allVotes")
    suspend fun findAllVotesByPlannerId(
        @Path("planner_id") plannerId: Long,
    ): BaseResponse<FindAllVotesByPlannerIdResponseList>

    @PUT("/api/v1/planners/{planner_id}/{user_id}")
    suspend fun addUsersToPlanner(
        @Path("planner_id") plannerId: Long,
        @Path("user_id") userId: String,
    ): BaseResponse<Long>

    @DELETE("/api/v1/planners/{planner_id}/{user_id}/exit")
    suspend fun exitUserFromPlanner(
        @Path("planner_id") plannerId: Long,
        @Path("user_id") userId: String,
    ): BaseResponse<Boolean>

    @DELETE("/api/v1/planners/{planner_id}")
    suspend fun deletePlannerById(
        @Path("planner_id") plannerId: Long,
    ): BaseResponse<Long>

    @DELETE("/api/v1/planners/{planner_id}/{user_id}/delete")
    suspend fun deletePlannerAndExitById(
        @Path("planner_id") plannerId: Long,
        @Path("user_id") userId: String,
    ): BaseResponse<Long>

}