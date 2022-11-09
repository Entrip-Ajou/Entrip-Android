package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.dto.NoticeResponse
import ajou.paran.entrip.repository.network.dto.NoticesSaveRequest
import ajou.paran.entrip.repository.network.dto.NoticesUpdateRequest
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure

interface NoticeRepository {
    suspend fun saveNotice(noticesSaveRequest: NoticesSaveRequest) : BaseResult<NoticeResponse, Failure>
    suspend fun updateNotice(notice_id : Long, noticesUpdateRequest: NoticesUpdateRequest) : BaseResult<NoticeResponse, Failure>
    suspend fun deleteNotice(notice_id : Long) : BaseResult<Long, Failure>
    suspend fun fetchAllNotices(planner_id : Long) : BaseResult<List<NoticeResponse>, Failure>
    suspend fun fetchNotice(notice_id : Long) : BaseResult<NoticeResponse, Failure>
}