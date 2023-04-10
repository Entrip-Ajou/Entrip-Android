package ajou.paran.data.remote.api

import ajou.paran.data.remote.model.request.SaveNoticeRequest
import ajou.paran.data.remote.model.request.UpdateNoticeByIdRequest
import ajou.paran.data.remote.model.response.FetchAllNoticesByPlannerIdResponseList
import ajou.paran.data.remote.model.response.FetchNoticeByIdResponse
import ajou.paran.data.remote.model.response.SaveNoticeResponse
import ajou.paran.data.remote.model.response.UpdateNoticeByIdResponse
import retrofit2.http.*

interface NoticeAPI {

    @POST("api/v1/notices")
    suspend fun saveNotice(
        @Body request: SaveNoticeRequest,
    ): SaveNoticeResponse

    @PUT("api/v1/notices/{notice_id}")
    suspend fun updateNoticeById(
        @Path("notice_id") noticeId: Long,
        @Body request: UpdateNoticeByIdRequest
    ): UpdateNoticeByIdResponse

    @DELETE("api/v1/notices/{notice_id}")
    suspend fun deleteNoticeById(
        @Path("notice_id") noticeId: Long,
    ): Long

    @GET("api/v1/planners/{planner_id}/allNotices")
    suspend fun fetchAllNoticesByPlannerId(
        @Path("planner_id") plannerId: Long,
    ): FetchAllNoticesByPlannerIdResponseList

    @GET("api/v1/notices/{notice_id}")
    suspend fun fetchNoticeById(
        @Path("notice_id") noticeId : Long
    ): FetchNoticeByIdResponse

}