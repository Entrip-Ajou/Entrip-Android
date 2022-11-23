package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.base.BaseUrl
import ajou.paran.entrip.repository.network.dto.BaseResponse
import ajou.paran.entrip.repository.network.dto.community.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface CommunityApi {

    @Multipart
    @POST(BaseUrl.V1.Community.PHOTO_SAVE_URL)
    suspend fun savePhoto(
        @Path("priority") priority: Long = 1,
        @Part photos: MultipartBody.Part
    ): BaseResponse<Long>

    @GET(BaseUrl.V1.Community.PHOTO_FIND_URL)
    suspend fun findByIdPhoto(
        @Path("photo_id") photoId: Long
    ): BaseResponse<ResponseFindByIdPhoto>

    @DELETE(BaseUrl.V1.Community.PHOTO_DELETE_URL)
    suspend fun deletePhoto(
        @Path("photo_id") photoId: Long
    ): BaseResponse<Long>

    @PUT(BaseUrl.V1.Community.PHOTO_ADD_POST_URL)
    suspend fun addPostsToPhotos(
        @Path("photo_id") photoId: Long,
        @Path("post_id") postId: Long
    ): BaseResponse<Boolean>

    @POST(BaseUrl.V1.Community.POST_SAVE_URL)
    suspend fun savePost(
        @Body requestPost: RequestPost
    ): BaseResponse<Long>

    @GET(BaseUrl.V1.Community.POST_FIND_URL)
    suspend fun findByIdPost(
        @Path("post_id") postId: Long
    ): BaseResponse<ResponsePost>

    @DELETE(BaseUrl.V1.Community.POST_DELETE_URL)
    suspend fun deletePost(
        @Path("post_id") postId: Long
    ): BaseResponse<Long>

    @GET(BaseUrl.V1.Community.POST_GET_PAGE_URL)
    suspend fun getPostsListWithPageNum(
        @Path("pageNumber") pageNum: Long
    ): BaseResponse<List<ResponsePost>>

    @PUT(BaseUrl.V1.Community.POST_RAISE_LIKE_URL)
    suspend fun raiseLikeWithPostId(
        @Path("post_id") postId: Long,
        @Path("user_id") user_id: String
    ): BaseResponse<Long>

    @PUT(BaseUrl.V1.Community.POST_DECREASE_LIKE_URL)
    suspend fun decreaseLikeWithPostId(
        @Path("post_id") postId: Long,
        @Path("user_id") user_id: String
    ): BaseResponse<Long>

    @POST(BaseUrl.V1.Community.COMMENT_SAVE_URL)
    suspend fun saveComment(
        @Body requestComment: RequestComment
    ): BaseResponse<Long>

    @GET(BaseUrl.V1.Community.COMMENT_FIND_URL)
    suspend fun findByIdComment(
        @Path("postComment_id") commentId: Long
    ): BaseResponse<ResponseComment>

    @DELETE(BaseUrl.V1.Community.COMMENT_DELETE_URL)
    suspend fun deleteComment(
        @Path("postComment_id") commentId: Long
    ): BaseResponse<Long>

    @GET(BaseUrl.V1.Community.COMMENT_GET_ALL_URL)
    suspend fun getAllCommentsWithPostId(
        @Path("post_id") postId: Long
    ): BaseResponse<List<ResponseComment>>

    @POST(BaseUrl.V1.Community.NESTED_COMMENT_SAVE_URL)
    suspend fun saveNestedComment(
        @Body requestComment: RequestNestedComment
    ): BaseResponse<Long>

    @GET(BaseUrl.V1.Community.NESTED_COMMENT_FIND_URL)
    suspend fun findByIdNestedComment(
        @Path("postNestedComment_id") nestedCommentId: Long
    ): BaseResponse<ResponseNestedComment>

    @DELETE(BaseUrl.V1.Community.NESTED_COMMENT_DELETE_URL)
    suspend fun deleteNestedComment(
        @Path("postNestedComment_id") nestedCommentId: Long
    ): BaseResponse<Long>

    @GET(BaseUrl.V1.Community.NESTED_COMMENT_GET_ALL_URL)
    suspend fun getAllNestedCommentsWithPostCommentId(
        @Path("postComment_id") commentId: Long
    ): BaseResponse<List<ResponseNestedComment>>
}