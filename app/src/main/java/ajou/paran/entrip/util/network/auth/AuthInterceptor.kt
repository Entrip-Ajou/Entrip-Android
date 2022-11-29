package ajou.paran.entrip.util.network.auth

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor
@Inject
constructor(
    private val sharedPreferences: SharedPreferences
) : Interceptor {

    private val accessToken: String
        get() = if (sharedPreferences.getString("accessToken", "").isNullOrEmpty()) "" else sharedPreferences.getString("accessToken", "")!!

    override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
        val request = request().newBuilder()
            .addHeader("AccessToken", accessToken)
            .build()
        proceed(request)
    }
}