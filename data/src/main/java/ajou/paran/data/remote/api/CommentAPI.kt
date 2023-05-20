package ajou.paran.data.remote.api

import ajou.paran.data.remote.model.request.InsertCommentRequest
import ajou.paran.data.remote.model.response.DeleteCommentByIdResponse
import ajou.paran.data.remote.model.response.InsertCommentResponse
import ajou.paran.data.remote.model.response.SelectCommentByPlanIdResponseList
import retrofit2.http.*

interface CommentAPI {

    @POST("api/v1/comments")
    suspend fun insertComment(
        @Body request: InsertCommentRequest,
    ): InsertCommentResponse

    @DELETE("api/v1/comments/{comment_id}")
    suspend fun deleteCommentById(
        @Path("comment_id") commentId: Long,
    ): DeleteCommentByIdResponse

    @GET("api/v1/comments/{plan_id}/getAllComments")
    suspend fun selectCommentByPlanId(
        @Path("plan_id") planId: Long
    ): SelectCommentByPlanIdResponseList

}