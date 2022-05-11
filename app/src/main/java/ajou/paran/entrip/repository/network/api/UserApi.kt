package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.repository.network.dto.BaseResponse
import ajou.paran.entrip.repository.network.dto.UserRequest
import ajou.paran.entrip.repository.network.dto.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @GET("api/v1/users/{user_id}/user_id/exist")
    suspend fun isExistUserId(
        @Path("user_id") user_id: String
    ) : BaseResponse<Boolean>

    @GET("api/v1/users/{nickname}/nickname/exist")
    suspend fun isExistNickname(
        @Path("nickname") nickname: String
    ) : BaseResponse<Boolean>

    @POST("api/v1/users")
    suspend fun saveUser(
        @Body userResponse: UserResponse
    ) : BaseResponse<UserRequest>
}