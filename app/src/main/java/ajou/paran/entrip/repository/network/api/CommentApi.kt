package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.base.BaseUrl
import ajou.paran.entrip.repository.network.dto.BaseResponse
import ajou.paran.entrip.repository.network.dto.CommentPlanResponse
import ajou.paran.entrip.repository.network.dto.CommentRequest
import ajou.paran.entrip.repository.network.dto.CommentResponse
import retrofit2.http.*

interface CommentApi {

    @POST(BaseUrl.V1.Comment.COMMENT_SAVE_URL)
    suspend fun insertComment(
        @Body commentRequest: CommentRequest
    ): BaseResponse<CommentPlanResponse>

    @DELETE(BaseUrl.V1.Comment.COMMENT_DELETE_URL)
    suspend fun deleteComment(
        @Path("comment_id") comment_id: Long
    ): BaseResponse<CommentPlanResponse>

    @GET(BaseUrl.V1.Comment.COMMENT_SELECT_URL)
    suspend fun selectComment(
        @Path("plan_id") plan_id: Long
    ): BaseResponse<List<CommentResponse>>

}