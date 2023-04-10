package ajou.paran.data.remote.api

import ajou.paran.data.remote.model.request.PostNotificationRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmAPI {

    @POST("fcm/send")
    suspend fun postNotification(
        @Body request: PostNotificationRequest
    )

}