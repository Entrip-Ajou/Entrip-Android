package ajou.paran.data.remote.datasource

import ajou.paran.data.remote.model.request.NotificationRequest

interface FcmRemoteDataSource {

    suspend fun postNotification(
        data: NotificationRequest,
        to: String
    )

}