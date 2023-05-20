package ajou.paran.data.remote.datasource

interface AuthRemoteDataSource {

    suspend fun reissueAccessTokenByRefreshToken(
        refreshToken: String,
    )

}