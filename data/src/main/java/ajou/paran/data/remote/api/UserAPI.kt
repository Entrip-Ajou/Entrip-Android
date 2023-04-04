package ajou.paran.data.remote.api

import ajou.paran.data.remote.model.request.SaveUserAccountRequest
import ajou.paran.data.remote.model.request.SaveUserRequest
import ajou.paran.data.remote.model.request.SignInByUserAccountRequest
import ajou.paran.data.remote.model.response.*
import retrofit2.http.*

interface UserAPI {

    //region v2
    @Headers("No-Authentication: true")
    @POST("api/v2/users")
    suspend fun saveUserAccount(
        @Body request: SaveUserAccountRequest
    ): SaveUserAccountResponse

    @Headers("No-Authentication: true")
    @POST("api/v2/users/login")
    suspend fun signInByUserAccount(
        @Body request: SignInByUserAccountRequest
    ): SignInByUserAccountResponse

    @Headers("No-Authentication: true")
    @GET("api/v2/users/{user_id}/user_id/exist")
    suspend fun isExistUserByUserId(
        @Path("user_id") userId: String
    ): Boolean

    @Headers("No-Authentication: true")
    @GET("api/v2/users/{nickname}/nickname/exist")
    suspend fun isExistUserByNickname(
        @Path("nickname") nickname: String
    ): Boolean

    //endregion

    //region v1
    @POST("api/v1/users")
    suspend fun saveUser(
        @Body response: SaveUserResponse
    ): SaveUserRequest

    @PUT("api/v1/users/token/{user_id}/{token}")
    suspend fun updateToken(
        @Path("user_id") user_id : String,
        @Path("token") token : String
    )

    @GET("api/v1/users/findUserWithNicknameOrUserId/{user_id_or_nickname}")
    suspend fun searchUser(
        @Path("user_id_or_nickname") user_id_or_nickname : String
    ): SearchUserResponse

    @GET("api/v1/planners/{planner_id}/getAllUser")
    suspend fun findAllUsersByPlannerId(
        @Path("planner_id") planner_id : Long
    ): FindAllUsersWithPlannerIdResponseList

    @PUT("api/v1/planners/{planner_id}/{user_id}")
    suspend fun addPlanners(
        @Path("planner_id") planner_id : Long,
        @Path("user_id") user_id : String
    ): Int

    @GET("api/v1/users/{user_id}/all")
    suspend fun findAllPlannersByUser(
        @Path("user_id") user_id: String
    ): FindAllPlannersResponseList

    @GET("api/v1/users/{user_id}")
    suspend fun findUserById(
        @Path("user_id") user_id : String
    ): FindUserByIdResponse

    @GET("api/v1/planners/{planner_id}/{user_id}/exist")
    suspend fun isExistUserInPlannerByUserId(
        @Path ("planner_id") planner_id : Long,
        @Path ("user_id") user_id : String
    ): Boolean

    @GET("api/v1/planners/{planner_id}/{nickname}/exist")
    suspend fun isExistUserInPlannerByNickname(
        @Path ("planner_id") planner_id : Long,
        @Path ("nickname") nickname : String
    ): Boolean
    //endregion

}