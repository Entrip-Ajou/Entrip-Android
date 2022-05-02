package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.repository.network.dto.BaseResponse
import ajou.paran.entrip.repository.network.dto.PlanRequest
import ajou.paran.entrip.repository.network.dto.PlanResponse
import ajou.paran.entrip.repository.network.dto.planner.CreatePlannerDto
import ajou.paran.entrip.repository.network.dto.planner.PlannerData
import ajou.paran.entrip.repository.network.dto.planner.UpdatePlannerRequestDto
import retrofit2.Response
import retrofit2.http.*

interface PlanApi {
    companion object{
        const val BASE_URL = "http://2ntrip.com/"
    }

    // Planner 추가를 눌렀을 때
    @POST("api/v1/planners")
    suspend fun createPlanner(
        @Body user_id: CreatePlannerDto
    ) : BaseResponse<PlannerData>

    @POST("api/v1/users")
    suspend fun insertUser(
        @Body user_id : CreatePlannerDto
    ) : BaseResponse<CreatePlannerDto>

    // Home화면 Planner 선택란에서 기존에 저장된 Planner를 눌렀을 때
    @GET("api/v1/plans/{planner_id}")
    suspend fun fetchPlanner(
        @Path("planner_id") planner_id : Long
    ) : BaseResponse<PlannerData>

    @GET("api/v1/planners/{planner_id}/all")
    suspend fun fetchPlans(
        @Path("planner_id") planner_idFK : Long
    ) : Response<List<PlanResponse>>

    @GET("api/v1/planners/{planner_id}/exist")
    suspend fun isExist(
        @Path("planner_id") planner_id: Long
    ) : BaseResponse<Boolean>

    @POST("api/v1/plans")
    suspend fun insertPlan(
        @Body plan : PlanRequest
    ) : Response<PlanResponse>

    @DELETE("api/v1/plans/{plan_id}")
    suspend fun deletePlan(
        @Path("plan_id") plan_id : Long
    ) : Response<Long>

    @DELETE("api/v1/planners/{planner_id}")
    suspend fun deletePlanner(
        @Path("planner_id") planner_id: Long
    ) : BaseResponse<Long>

    /**
     * update 사용할 때, 문제 발생 시 Entity class -> Serialized(dto) class 변경하기
     */

    @PUT("api/v1/plans/{plan_id}")
    suspend fun updatePlan(
        @Path("plan_id") plan_id: Long,
        @Body plan:PlanEntity
    ) : Response<PlanEntity>

    @PUT("api/v1/planners/{planner_id}")
    suspend fun updatePlanner(
        @Path("planner_id") planner_id : Long,
        @Body updatePlannerRequestDto: UpdatePlannerRequestDto
    ) : BaseResponse<PlannerData>

    @GET("api/v1/planners/{planner_id}")
    suspend fun findPlanner(
        @Path("planner_id") planner_id: Long
    ) : BaseResponse<PlannerData>

}