package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.repository.network.dto.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {
    companion object{
        const val FCM_URL = "https://fcm.googleapis.com/"
    }

    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification : PushNotification
    ) : Response<ResponseBody>
}