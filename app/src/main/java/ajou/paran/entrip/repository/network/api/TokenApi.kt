package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.base.BaseUrl
import ajou.paran.entrip.repository.network.dto.BaseResponse
import ajou.paran.entrip.repository.network.dto.response.UserReissueAccessTokenResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface TokenApi {
    // Reissue User AccessToken using refreshToken
    @GET(BaseUrl.V2.User.REISSUE_URL)
    suspend fun reissueUserAccessToken(
        @Path("refreshToken") refreshToken: String
    ): BaseResponse<UserReissueAccessTokenResponseDto>
}