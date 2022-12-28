package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.dto.community.*
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import android.graphics.Bitmap
import okhttp3.MultipartBody
import java.io.File

interface CommunityRepository {

    suspend fun savePhoto(
        priority: Long,
        image: MultipartBody.Part,
        attempt: Int = 0
    ): BaseResult<Long, Failure>

    suspend fun findByIdPhoto(
        photoId: Long,
        attempt: Int = 0
    ): BaseResult<ResponseFindByIdPhoto, Failure>

    suspend fun deletePhoto(
        photoId: Long
    ): BaseResult<Long, Failure>

    suspend fun addPostsToPhotos(
        photoId: Long,
        postId: Long
    ): BaseResult<Boolean, Failure>

    suspend fun savePost(
        requestPost: RequestPost,
        attempt: Int = 0
    ): BaseResult<Long, Failure>

    suspend fun findByIdPost(
        postId: Long,
        attempt: Int = 0
    ): BaseResult<ResponsePost, Failure>

    suspend fun deletePost(
        postId: Long
    ): BaseResult<Long, Failure>

    suspend fun getPostsListWithPageNum(
        pageNum: Long
    ): BaseResult<List<ResponsePost>, Failure>

    suspend fun raiseLikeWithPostId(
        postId: Long,
        user_id: String
    ): BaseResult<Long, Failure>

    suspend fun decreaseLikeWithPostId(
        postId: Long,
        user_id: String
    ): BaseResult<Long, Failure>

    suspend fun saveComment(
        author: String,
        content: String,
        postId: Long,
        attempt: Int = 0
    ): BaseResult<Long, Failure>

    suspend fun findByIdComment(
        commentId: Long
    ): BaseResult<ResponseComment, Failure>

    suspend fun deleteComment(
        commentId: Long
    ): BaseResult<Long, Failure>

    suspend fun getAllCommentsWithPostId(
        postId: Long,
        attempt: Int = 0
    ): BaseResult<List<ResponseComment>, Failure>

    suspend fun saveNestedComment(
        author: String,
        content: String,
        commentId: Long,
        attempt: Int = 0
    ): BaseResult<Long, Failure>

    suspend fun findByIdNestedComment(
        nestedCommentId: Long
    ): BaseResult<ResponseNestedComment, Failure>

    suspend fun deleteNestedComment(
        nestedCommentId: Long
    ): BaseResult<Long, Failure>

    suspend fun getAllNestedCommentsWithPostCommentId(
        commentId: Long,
        attempt: Int = 0
    ): BaseResult<List<ResponseNestedComment>, Failure>

}