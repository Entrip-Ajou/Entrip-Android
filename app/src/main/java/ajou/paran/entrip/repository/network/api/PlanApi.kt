package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.dto.*
import retrofit2.http.*


interface PlanApi {
    companion object{
        const val BASE_URL = "http://2ntrip.com/"
    }

    // Planner 추가를 눌렀을 때
    @POST("api/v1/planners")
    suspend fun createPlanner(@Body user_id : String) : BaseResponse<PlannerResponse>

    // Home화면 Planner 선택란에서 기존에 저장된 Planner를 눌렀을 때
    @GET("api/v1/planners/{planner_id}")
    suspend fun fetchPlanner(@Path("planner_id") planner_id : Long) : BaseResponse<PlannerResponse>

    @GET("api/v1/planners/{planner_id}/all")
    suspend fun fetchPlans(@Path("planner_id") planner_idFK : Long) : BaseResponse<List<PlanResponse>>

    @GET("api/v1/planners/{planner_id}/exist")
    suspend fun isExist(@Path("planner_id") planner_id: Long) : BaseResponse<Boolean>

    @POST("api/v1/plans")
    suspend fun insertPlan(@Body plan : PlanRequest) : BaseResponse<PlanResponse>

    @DELETE("api/v1/plans/{plan_id}")
    suspend fun deletePlan(@Path("plan_id")plan_id : Long) : BaseResponse<Long>

    @PUT("api/v1/plans/{plan_id}")
    suspend fun updatePlan(@Path("plan_id") plan_id: Long, @Body plan: PlanUpdateRequest) : BaseResponse<PlanResponse>

    @PUT("api/v1/planners/{planner_id}")
    suspend fun updatePlanner(@Path("planner_id") planner_id : Long, @Body plannerEntity: PlannerEntity) : BaseResponse<PlannerEntity>
}