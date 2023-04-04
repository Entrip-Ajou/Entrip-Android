package ajou.paran.data.utils.interceptors

import ajou.paran.data.local.datasource.LocalUserDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor
@Inject
constructor(
    private val localUserDataSource: LocalUserDataSource
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
        var request = request()

        if (request.header("No-Authentication") == null) {
            if (request.header("idToken") != null) {
                val idToken = runBlocking {
                    localUserDataSource.fetchIdToken().first()
                }

                request = request.newBuilder()
                    .addHeader(
                        name = "token",
                        value = idToken
                    )
                    .build()
            } else if (request.header("refreshToken") != null) {
                val refreshToken = runBlocking {
                    localUserDataSource.fetchRefreshToken().first()
                }

                request = request.newBuilder()
                    .addHeader(
                        name = "token",
                        value = refreshToken
                    )
                    .build()
            } else {
                val accessToken = runBlocking {
                    localUserDataSource.fetchAccessToken().first()
                }

                request = request.newBuilder()
                    .addHeader(
                        name = "token",
                        value = accessToken
                    )
                    .build()
            }

        }

        proceed(request)
    }
}
