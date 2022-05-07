package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.repository.network.dto.BaseResponse
import ajou.paran.entrip.repository.network.dto.planner.CreatePlannerDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {

    @GET("api/v1/users/{user_id}")
    suspend fun findUserId(
        @Path("user_id") user_id: String
    ) : BaseResponse<Boolean>

    @GET("")
    suspend fun checkNickName(
        @Body nickName: String
    ) : BaseResponse<Boolean>

    @GET("")
    suspend fun insertNickName(
        @Body user_id: String,
        @Body nickName: String
    ) : BaseResponse<Int>
}