package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.repository.network.dto.BaseResponse
import ajou.paran.entrip.repository.network.dto.CommentPlanResponse
import ajou.paran.entrip.repository.network.dto.CommentRequest
import ajou.paran.entrip.repository.network.dto.CommentResponse
import retrofit2.http.*

interface CommentApi {
    @POST("api/v1/comments")
    suspend fun insertComment(
        @Body commentRequest: CommentRequest
    ): BaseResponse<CommentPlanResponse>

    @DELETE("api/v1/comments/{comment_id}")
    suspend fun deleteComment(
        @Path("comment_id") comment_id: Long
    ): BaseResponse<CommentPlanResponse>

    @GET("api/v1/comments/{plan_id}/getAllComments")
    suspend fun selectComment(
        @Path("plan_id") plan_id: Long
    ): BaseResponse<List<CommentResponse>>
}