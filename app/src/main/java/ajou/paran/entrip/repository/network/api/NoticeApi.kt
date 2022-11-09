package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.repository.network.dto.BaseResponse
import ajou.paran.entrip.repository.network.dto.NoticeResponse
import ajou.paran.entrip.repository.network.dto.NoticesSaveRequest
import ajou.paran.entrip.repository.network.dto.NoticesUpdateRequest
import retrofit2.http.*

interface NoticeApi {
    @POST("api/v1/notices")
    suspend fun saveNotice(
        @Body noticesSaveRequest: NoticesSaveRequest
    ) : BaseResponse<NoticeResponse>

    @PUT("api/v1/notices/{notice_id}")
    suspend fun updateNotice(
        @Path("notice_id") notice_id : Long,
        @Body noticesUpdateRequest: NoticesUpdateRequest
    ) : BaseResponse<NoticeResponse>

    @DELETE("api/v1/notices/{notice_id}")
    suspend fun deleteNotice(
        @Path("notice_id") notice_id : Long
    ) : BaseResponse<Long>

    @GET("api/v1/planners/{planner_id}/allNotices")
    suspend fun fetchAllNotices(
        @Path("planner_id") planner_id : Long
    ) : BaseResponse<List<NoticeResponse>>

    @GET("api/v1/notices/{notice_id}")
    suspend fun fetchNotice(
        @Path("notice_id") notice_id : Long
    ) : BaseResponse<NoticeResponse>
}