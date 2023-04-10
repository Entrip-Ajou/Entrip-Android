package ajou.paran.data.remote.api

import ajou.paran.data.remote.model.request.SaveCommentRequest
import ajou.paran.data.remote.model.request.SaveNestedCommentRequest
import ajou.paran.data.remote.model.request.SavePostRequest
import ajou.paran.data.remote.model.response.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface CommunityAPI {

    @Multipart
    @POST("api/v1/photos/{priority}")
    suspend fun savePhoto(
        @Path("priority") priority: Long = 1,
        @Part photo: MultipartBody.Part,
    ): Long

    @GET("api/v1/photos/{photo_id}")
    suspend fun findPhotoById(
        @Path("photo_id") photoId: Long,
    ): FindPhotoByIdResponse

    @DELETE("api/v1/photos/{photo_id}")
    suspend fun deletePhoto(
        @Path("photo_id") photoId: Long
    ): Long

    @PUT("api/v1/photos/{photo_id}/{post_id}/addPosts")
    suspend fun addPhotoToPostById(
        @Path("photo_id") photoId: Long,
        @Path("post_id") postId: Long,
    ): Boolean

    @POST("api/v1/posts")
    suspend fun savePost(
        @Body request: SavePostRequest
    ): Long

    @GET("api/v1/posts/{post_id}")
    suspend fun findPostById(
        @Path("post_id") postId: Long,
    ): FindPostByIdResponse

    @DELETE("api/v1/posts/{post_id}")
    suspend fun deletePostById(
        @Path("post_id") postId: Long,
    ): Long

    @GET("api/v1/posts/{pageNumber}/all")
    suspend fun getPostsListByPageNum(
        @Path("pageNumber") pageNum: Long
    ): GetPostsListByPageNumResponseList

    @PUT("api/v1/posts/{post_id}/{user_id}/like")
    suspend fun raiseLikeWithPostId(
        @Path("post_id") postId: Long,
        @Path("user_id") userId: String,
    ): Long

    @PUT("api/v1/posts/{post_id}/{user_id}/dislike")
    suspend fun decreaseLikeWithPostId(
        @Path("post_id") postId: Long,
        @Path("user_id") userId: String,
    ): Long

    @POST("api/v1/postsComments")
    suspend fun saveComment(
        @Body request: SaveCommentRequest
    ): Long

    @GET("api/v1/postsComments/{postComment_id}")
    suspend fun findCommentById(
        @Path("postComment_id") commentId: Long,
    ): FindCommentByIdResponse

    @DELETE("api/v1/postsComments/{postComment_id}")
    suspend fun deleteCommentById(
        @Path("postComment_id") commentId: Long,
    ): Long

    @GET("api/v1/postsComments/{post_id}/all")
    suspend fun getAllCommentsByPostId(
        @Path("post_id") postId: Long,
    ): GetAllCommentsByPostIdResponseList

    @POST("api/v1/postsNestedComments")
    suspend fun saveNestedComment(
        @Body request: SaveNestedCommentRequest,
    ): Long

    @GET("api/v1/postsNestedComments/{postNestedComment_id}")
    suspend fun findNestedCommentById(
        @Path("postNestedComment_id") nestedCommentId: Long,
    ): FindNestedCommentByIdResponse

    @DELETE("api/v1/postsNestedComments/{postNestedComment_id}")
    suspend fun deleteNestedCommentById(
        @Path("postNestedComment_id") nestedCommentId: Long,
    ): Long

    @GET("api/v1/postsNestedComments/{postComment_id}/all")
    suspend fun getAllNestedCommentsByPostCommentId(
        @Path("postComment_id") commentId: Long,
    ): GetAllNestedCommentsByPostCommentIdResponseList

}