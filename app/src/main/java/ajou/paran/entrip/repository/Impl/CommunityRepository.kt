package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.dto.community.RequestPost
import ajou.paran.entrip.repository.network.dto.community.ResponseFindByIdPhoto
import ajou.paran.entrip.repository.network.dto.community.ResponsePost
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import android.graphics.Bitmap
import okhttp3.MultipartBody

interface CommunityRepository {

    suspend fun savePhoto(
        priority: Long,
        image: MultipartBody.Part,
        attempt: Int = 0
    ): Long

    suspend fun findByIdPhoto(
        photoId: Long,
        attempt: Int = 0
    ): ResponseFindByIdPhoto

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
    ): Long

    suspend fun findByIdPost(
        postId: Long,
        attempt: Int = 0
    ): ResponsePost

    suspend fun deletePost(
        postId: Long
    ): BaseResult<Long, Failure>

    suspend fun getPostsListWithPageNum(
        pageNum: Long
    ): List<ResponsePost>

    suspend fun raiseLikeWithPostId(
        postId: Long,
        user_id: String
    ): BaseResult<Long, Failure>

    suspend fun decreaseLikeWithPostId(
        postId: Long,
        user_id: String
    ): BaseResult<Long, Failure>

}