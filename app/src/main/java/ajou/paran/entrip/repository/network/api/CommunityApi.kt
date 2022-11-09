package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.repository.network.dto.BaseResponse
import ajou.paran.entrip.repository.network.dto.community.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface CommunityApi {

    @Multipart
    @POST("api/v1/photos/{priority}")
    suspend fun savePhoto(
        @Path("priority") priority: Long = 1,
        @Part photos: MultipartBody.Part
    ): BaseResponse<Long>

    @GET("api/v1/photos/{photo_id}")
    suspend fun findByIdPhoto(
        @Path("photo_id") photoId: Long
    ): BaseResponse<ResponseFindByIdPhoto>

    @DELETE("api/v1/photos/{photo_id}")
    suspend fun deletePhoto(
        @Path("photo_id") photoId: Long
    ): BaseResponse<Long>

    @PUT("api/v1/photos/{photo_id}/{post_id}/addPosts")
    suspend fun addPostsToPhotos(
        @Path("photo_id") photoId: Long,
        @Path("post_id") postId: Long
    ): BaseResponse<Boolean>

    @POST("api/v1/posts")
    suspend fun savePost(
        @Body requestPost: RequestPost
    ): BaseResponse<Long>

    @GET("api/v1/posts/{post_id}")
    suspend fun findByIdPost(
        @Path("post_id") postId: Long
    ): BaseResponse<ResponsePost>

    @DELETE("api/v1/posts/{post_id}")
    suspend fun deletePost(
        @Path("post_id") postId: Long
    ): BaseResponse<Long>

    @GET("api/v1/posts/{pageNumber}/all")
    suspend fun getPostsListWithPageNum(
        @Path("pageNumber") pageNum: Long
    ): BaseResponse<List<ResponsePost>>

    @PUT("api/v1/posts/{post_id}/{user_id}/like")
    suspend fun raiseLikeWithPostId(
        @Path("post_id") postId: Long,
        @Path("user_id") user_id: String
    ): BaseResponse<Long>

    @PUT("api/v1/posts/{post_id}/{user_id}/dislike")
    suspend fun decreaseLikeWithPostId(
        @Path("post_id") postId: Long,
        @Path("user_id") user_id: String
    ): BaseResponse<Long>

    @POST("api/v1/postsComments")
    suspend fun saveComment(
        @Body requestComment: RequestComment
    ): BaseResponse<Long>

    @GET("api/v1/postsComments/{postComment_id}")
    suspend fun findByIdComment(
        @Path("postComment_id") commentId: Long
    ): BaseResponse<ResponseComment>

    @DELETE("api/v1/postsComments/{postComment_id}")
    suspend fun deleteComment(
        @Path("postComment_id") commentId: Long
    ): BaseResponse<Long>

    @GET("api/v1/postsComments/{post_id}/all")
    suspend fun getAllCommentsWithPostId(
        @Path("post_id") postId: Long
    ): BaseResponse<List<ResponseComment>>

    @POST("api/v1/postsNestedComments")
    suspend fun saveNestedComment(
        @Body requestComment: RequestNestedComment
    ): BaseResponse<Long>

    @GET("api/v1/postsNestedComments/{postNestedComment_id}")
    suspend fun findByIdNestedComment(
        @Path("postNestedComment_id") nestedCommentId: Long
    ): BaseResponse<ResponseNestedComment>

    @DELETE("api/v1/postsNestedComments/{postNestedComment_id}")
    suspend fun deleteNestedComment(
        @Path("postNestedComment_id") nestedCommentId: Long
    ): BaseResponse<Long>

    @GET("api/v1/postsNestedComments/{postComment_id}/all")
    suspend fun getAllNestedCommentsWithPostCommentId(
        @Path("postComment_id") commentId: Long
    ): BaseResponse<List<ResponseNestedComment>>
}