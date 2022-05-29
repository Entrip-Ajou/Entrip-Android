package ajou.paran.entrip.util.network.kakao

import ajou.paran.entrip.util.network.fcm.FcmInterceptor
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class KakaoInterceptor : Interceptor {
    companion object{
        const val API_KEY = "KakaoAK b79bc85deab48197458992f20d4e3277"
    }

    @Throws(IOException :: class)
    override fun intercept(chain: Interceptor.Chain)
            : Response = with(chain){
        val newRequest = request().newBuilder()
            .addHeader("Authorization", "${API_KEY}")
            .build()
        proceed(newRequest)
    }
}