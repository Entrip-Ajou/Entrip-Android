package ajou.paran.data.remote.datasource

import okhttp3.MultipartBody
import retrofit2.http.*


interface CommunityRemoteDataSource {

    suspend fun savePhoto(
        priority: Long = 1,
        photo: MultipartBody.Part,
    )

    suspend fun findPhotoById(
        photoId: Long,
    )

    suspend fun deletePhoto(
        photoId: Long,
    )

    suspend fun addPhotoToPostById(
        photoId: Long,
        postId: Long,
    )

    suspend fun savePost(
        title: String,
        content: String,
        author: String,
        photoIdList: List<Long>,
    )

    suspend fun findPostById(
        postId: Long,
    )

    suspend fun deletePostById(
        postId: Long,
    )

    suspend fun getPostsListByPageNum(
        @Path("pageNumber") pageNum: Long,
    )

    suspend fun raiseLikeWithPostId(
        postId: Long,
        userId: String,
    )

    suspend fun decreaseLikeWithPostId(
        postId: Long,
        userId: String,
    )

    suspend fun saveComment(
        author: String,
        content: String,
        postId: Long,
    )

    suspend fun findCommentById(
        commentId: Long,
    )

    suspend fun deleteCommentById(
        commentId: Long,
    )

    suspend fun getAllCommentsByPostId(
        postId: Long,
    )

    suspend fun saveNestedComment(
        author: String,
        content: String,
        commentId: Long,
    )

    suspend fun findNestedCommentById(
        nestedCommentId: Long,
    )

    suspend fun deleteNestedCommentById(
        nestedCommentId: Long,
    )

    suspend fun getAllNestedCommentsByPostCommentId(
        commentId: Long,
    )

}