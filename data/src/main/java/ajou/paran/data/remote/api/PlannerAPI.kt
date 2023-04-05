package ajou.paran.data.remote.api

import ajou.paran.data.remote.model.request.UpdatePlannerRequest
import ajou.paran.data.remote.model.response.*
import retrofit2.http.*

// api/v1/planners
// https://2ntrip.link
interface PlannerAPI {

    @POST("api/v1/planners/{user_id}")
    suspend fun createPlannerByUserId(
        @Path("user_id") userId: String
    ): CreatePlannerByUserIdResponse

    @DELETE("api/v1/planners/{planner_id}/{user_id}/exit")
    suspend fun deletePlanner(
        @Path("user_id") userId: String,
        @Path("planner_id") plannerId: Long
    ): Boolean

    @GET("api/v1/planners/{planner_id}")
    suspend fun fetchPlannerByPlannerId(
        @Path("planner_id") plannerId: Long
    ): FetchPlannerByPlannerIdResponse

    @GET("api/v1/planners/{planner_id}/all")
    suspend fun fetchPlansInPlannerByPlannerId(
        @Path("planner_id") plannerId: Long
    ): FetchPlansInPlannerByPlannerIdResponseList

    @PUT("api/v1/planners/{planner_id}")
    suspend fun updatePlanner(
        @Path("planner_id") plannerId: Long,
        @Body request: UpdatePlannerRequest
    ): UpdatePlannerResponse

    @GET("api/v1/planners/{planner_id}/{date}/find")
    suspend fun findPlanByPlannerIdWithDate(
        @Path("planner_id") plannerId: Long,
        @Path("date") date: String
    ): FindPlanByPlannerIdWithDateResponseList

    @GET("api/v1/planners/{planner_id}/exist")
    suspend fun isExistPlannerById(
        @Path("planner_id") plannerId: Long
    ): Boolean

}