package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.NoticeRemoteSource
import ajou.paran.entrip.repository.network.dto.NoticeResponse
import ajou.paran.entrip.repository.network.dto.NoticesSaveRequest
import ajou.paran.entrip.repository.network.dto.NoticesUpdateRequest
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import javax.inject.Inject

class NoticeRepositoryImpl
@Inject
constructor(
    private val noticeRemoteSource: NoticeRemoteSource
) : NoticeRepository {
    override suspend fun saveNotice(
        noticesSaveRequest: NoticesSaveRequest
    ): BaseResult<NoticeResponse, Failure> {
        val notice = noticeRemoteSource.saveNotice(noticesSaveRequest)

        when(notice){
            is BaseResult.Success -> return BaseResult.Success(notice.data)
            is BaseResult.Error -> return BaseResult.Error(Failure(notice.err.code, notice.err.message))
        }
    }

    override suspend fun updateNotice(
        notice_id: Long,
        noticesUpdateRequest: NoticesUpdateRequest
    ): BaseResult<NoticeResponse, Failure> {
        val res = noticeRemoteSource.updateNotice(notice_id, noticesUpdateRequest)

        when(res){
            is BaseResult.Success -> return BaseResult.Success(res.data)
            is BaseResult.Error -> return BaseResult.Error(Failure(res.err.code, res.err.message))
        }
    }

    override suspend fun deleteNotice(
        notice_id: Long
    ): BaseResult<Long, Failure> {
        val res = noticeRemoteSource.deleteNotice(notice_id)

        when(res){
            is BaseResult.Success -> return BaseResult.Success(res.data)
            is BaseResult.Error -> return BaseResult.Error(Failure(res.err.code, res.err.message))
        }
    }

    override suspend fun fetchAllNotices(
        planner_id: Long
    ): BaseResult<List<NoticeResponse>, Failure> {
        val res = noticeRemoteSource.fetchAllNotices(planner_id)

        when(res){
            is BaseResult.Success -> return BaseResult.Success(res.data)
            is BaseResult.Error -> return BaseResult.Error(Failure(res.err.code, res.err.message))
        }
    }

    override suspend fun fetchNotice(
        notice_id : Long
    ) : BaseResult<NoticeResponse, Failure> {
        val res = noticeRemoteSource.fetchNotice(notice_id)

        when(res){
            is BaseResult.Success -> return BaseResult.Success(res.data)
            is BaseResult.Error -> return BaseResult.Error(Failure(res.err.code, res.err.message))
        }
    }

}