package ajou.paran.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path

interface AuthAPI {

    @GET("api/v2/reIssue/{refreshToken}")
    suspend fun reissueAccessTokenByRefreshToken(
        @Path("refreshToken") refreshToken: String,
    ): String

}