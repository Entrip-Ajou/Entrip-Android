package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.remote.api.FcmAPI
import ajou.paran.data.remote.datasource.FcmRemoteDataSource
import ajou.paran.data.remote.model.request.NotificationRequest
import ajou.paran.data.utils.baseApiCall
import javax.inject.Inject

class FcmRemoteDataSourceImpl
@Inject
constructor(
    private val fcmAPI: FcmAPI,
) : FcmRemoteDataSource {

    override suspend fun postNotification(
        data: NotificationRequest,
        to: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }


}