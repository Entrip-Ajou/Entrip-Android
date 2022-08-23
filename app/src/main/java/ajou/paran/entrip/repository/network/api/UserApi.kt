package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.repository.network.dto.*
import ajou.paran.entrip.util.network.BaseResult
import retrofit2.http.*

interface UserApi {
    // todo : 실패시 202
    @GET("api/v1/users/{user_id}/user_id/exist")
    suspend fun isExistUserId(
        @Path("user_id") user_id: String
    ) : BaseResponse<Boolean>

    // todo : 실패시 202
    @GET("api/v1/users/{nickname}/nickname/exist")
    suspend fun isExistNickname(
        @Path("nickname") nickname: String
    ) : BaseResponse<Boolean>

    @POST("api/v1/users")
    suspend fun saveUser(
        @Body userResponse: UserTemp
    ) : BaseResponse<UserRequest>

    @PUT("api/v1/users/token/{user_id}/{token}")
    suspend fun updateToken(
        @Path("user_id") user_id : String,
        @Path("token") token : String
    ) : BaseResponse<Unit>

    // todo : 실패시 202
    @GET("api/v1/users/findUserWithNicknameOrUserId/{user_id_or_nickname}")
    suspend fun searchUser(
        @Path("user_id_or_nickname") user_id_or_nickname : String
    ) : BaseResponse<UserInformation>

    @GET("api/v1/planners/{planner_id}/getAllUser")
    suspend fun findAllUsersWithPlannerId(
        @Path("planner_id") planner_id : Long
    ) : BaseResponse<List<UserResponse>>

    @PUT("api/v1/planners/{planner_id}/{user_id}")
    suspend fun addPlanners(
        @Path("planner_id") planner_id : Long,
        @Path("user_id") user_id : String
    ) : BaseResponse<Int>

    @GET("api/v1/users/{user_id}/all")
    suspend fun findAllPlanners(
        @Path("user_id") user_id: String
    ) : BaseResponse<List<PlannerResponse>>

    @GET("api/v1/users/{user_id}")
    suspend fun findById(
        @Path("user_id") user_id : String
    ) : BaseResponse<UserResponse>

    @GET("api/v1/users/{user_id}")
    suspend fun findByUserId(
        @Path("user_id") user_id: String
    ) : BaseResponse<UserResponse>

    // todo : 실패시 202
    @GET("api/v1/planners/{planner_id}/{user_id}/exist")
    suspend fun userIsExistWithPlanner(
        @Path ("planner_id") planner_id : Long,
        @Path ("user_id") user_id : String
    ) : BaseResponse<Boolean>

    // todo : 실패시 202
    @GET("api/v1/planners/{planner_id}/{nickname}/exist")
    suspend fun userNickNameIsExistWithPlanner(
        @Path ("planner_id") planner_id : Long,
        @Path ("nickname") nickname : String
    ) : BaseResponse<Boolean>
}