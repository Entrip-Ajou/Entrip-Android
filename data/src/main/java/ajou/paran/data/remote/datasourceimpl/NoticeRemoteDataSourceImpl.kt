package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.remote.api.NoticeAPI
import ajou.paran.data.remote.datasource.NoticeRemoteDataSource
import ajou.paran.data.utils.baseApiCall
import javax.inject.Inject

class NoticeRemoteDataSourceImpl
@Inject
constructor(
    private val noticeAPI: NoticeAPI,
) : NoticeRemoteDataSource {

    override suspend fun saveNotice(
        author: String,
        title: String,
        content: String,
        plannerId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun updateNoticeById(
        noticeId: Long,
        title: String,
        content: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNoticeById(
        noticeId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAllNoticesByPlannerId(
        plannerId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun fetchNoticeById(
        noticeId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

}