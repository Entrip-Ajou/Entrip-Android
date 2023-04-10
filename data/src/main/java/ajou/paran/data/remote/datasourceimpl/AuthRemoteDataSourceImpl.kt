package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.remote.api.AuthAPI
import ajou.paran.data.remote.datasource.AuthRemoteDataSource
import ajou.paran.data.utils.baseApiCall
import javax.inject.Inject

class AuthRemoteDataSourceImpl
@Inject
constructor(
    private val authAPI: AuthAPI,
) : AuthRemoteDataSource {

    override suspend fun reissueAccessTokenByRefreshToken(
        refreshToken: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

}