package ajou.paran.data.remote.api

import ajou.paran.data.remote.model.request.AddPlanRequest
import ajou.paran.data.remote.model.request.UpdatePlanByIdRequest
import ajou.paran.data.remote.model.response.AddPlanResponse
import ajou.paran.data.remote.model.response.UpdatePlanByIdResponse
import retrofit2.http.*

// api/v1/plans

interface PlanAPI {

    @POST("api/v1/plans")
    suspend fun addPlan(
        @Body request: AddPlanRequest
    ): AddPlanResponse

    @DELETE("api/v1/plans/{plan_id}")
    suspend fun deletePlanById(
        @Path("plan_id") planId: Long
    ): Long

    @PUT("api/v1/plans/{plan_id}")
    suspend fun updatePlanById(
        @Path("plan_id") planId: Long,
        @Body plan: UpdatePlanByIdRequest
    ): UpdatePlanByIdResponse

}