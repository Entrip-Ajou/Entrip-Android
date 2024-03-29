package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.base.BaseUrl
import ajou.paran.entrip.repository.network.dto.BaseResponse
import ajou.paran.entrip.repository.network.dto.request.UserLoginRequestDto
import ajou.paran.entrip.repository.network.dto.request.UserSaveRequestDto
import ajou.paran.entrip.repository.network.dto.response.UserLoginResponseDto
import ajou.paran.entrip.repository.network.dto.response.UserSaveResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserAPIV2 {

    // Save User Account
    @POST(BaseUrl.V2.User.SAVE_URL)
    suspend fun saveUserAccount(
        @Body saveRequest: UserSaveRequestDto
    ): BaseResponse<UserSaveResponseDto>

    // Login User Account
    @POST(BaseUrl.V2.User.LOGIN_URL)
    suspend fun loginUserAccount(
        @Body loginRequest: UserLoginRequestDto
    ): BaseResponse<UserLoginResponseDto>

    @GET(BaseUrl.V2.User.IS_EXIST_ID_URL)
    suspend fun isExistUserId(
        @Path("user_id") userId: String
    ): BaseResponse<Boolean>

    @GET(BaseUrl.V2.User.IS_EXIST_NICKNAME_URL)
    suspend fun isExistNickname(
        @Path("nickname") nickname: String
    ): BaseResponse<Boolean>

}