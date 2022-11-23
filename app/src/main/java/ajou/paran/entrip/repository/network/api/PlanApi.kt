package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.base.BaseUrl
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.dto.*
import retrofit2.http.*

interface PlanApi {

    // Planner 추가를 눌렀을 때
    @POST(BaseUrl.V1.Plan.PLANNER_CREATE_URL)
    suspend fun createPlanner(@Path ("user_id") user_id : String) : BaseResponse<PlannerResponse>

    @DELETE(BaseUrl.V1.Plan.PLANNER_DELETE_URL)
    suspend fun deletePlanner(@Path("user_id") user_id : String, @Path("planner_id") planner_id : Long) : BaseResponse<Boolean>

    // Home화면 Planner 선택란에서 기존에 저장된 Planner를 눌렀을 때
    @GET(BaseUrl.V1.Plan.PLANNER_FETCH_URL)
    suspend fun fetchPlanner(@Path("planner_id") planner_id : Long) : BaseResponse<PlannerResponse>

    @GET(BaseUrl.V1.Plan.PLANNER_FETCH_PLANS_URL)
    suspend fun fetchPlans(@Path("planner_id") planner_idFK : Long) : BaseResponse<List<PlanResponse>>

    @PUT(BaseUrl.V1.Plan.PLANNER_UPDATE_URL)
    suspend fun updatePlanner(@Path("planner_id") planner_id : Long, @Body planner : PlannerUpdateRequest) : BaseResponse<PlannerEntity>

    @GET(BaseUrl.V1.Plan.PLANNER_FIND_DATE_URL)
    suspend fun findByPlannerIdWithDate(
        @Path("planner_id") planner_id:Long,
        @Path("date") date : String
    ) : BaseResponse<List<PlanResponse>>

    @GET(BaseUrl.V1.Plan.PLANNER_EXIST_URL)
    suspend fun isExist(@Path("planner_id") planner_id: Long) : BaseResponse<Boolean>

    @POST(BaseUrl.V1.Plan.PLAN_INSERT_URL)
    suspend fun insertPlan(@Body plan : PlanRequest) : BaseResponse<PlanResponse>

    @DELETE(BaseUrl.V1.Plan.PLAN_DELETE_URL)
    suspend fun deletePlan(@Path("plan_id")plan_id : Long) : BaseResponse<Long>

    @PUT(BaseUrl.V1.Plan.PLAN_UPDATE_URL)
    suspend fun updatePlan(@Path("plan_id") plan_id: Long, @Body plan: PlanUpdateRequest) : BaseResponse<PlanResponse>

}