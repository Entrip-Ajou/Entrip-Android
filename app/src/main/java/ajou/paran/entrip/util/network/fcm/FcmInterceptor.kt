package ajou.paran.entrip.util.network.fcm

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class FcmInterceptor : Interceptor {
    companion object{
        const val FCM_SERVER_KEY = "AAAA9n8w-p8:APA91bEPPPmWQm_nz8tkph8N0qvxWX_0aKXwYtLMppoxe-a2BWxBIo18WrGSjexjmKHeDYCdWe6i364Ko6QSOqryYP0IgID7iIRiuqpSZ4PTR3Mm3m9sIyGjRSbw9YAe_1i73yfbSWNX"
    }

    @Throws(IOException :: class)
    override fun intercept(chain: Interceptor.Chain)
            : Response = with(chain){
        val newRequest = request().newBuilder()
            .addHeader("Authorization", "key=${FCM_SERVER_KEY}")
            .addHeader("Content-Type", "application/json")
            .build()
        proceed(newRequest)
    }
}