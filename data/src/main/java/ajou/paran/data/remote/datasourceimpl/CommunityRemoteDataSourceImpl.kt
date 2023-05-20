package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.remote.api.CommunityAPI
import ajou.paran.data.remote.datasource.CommunityRemoteDataSource
import ajou.paran.data.utils.baseApiCall
import okhttp3.MultipartBody
import javax.inject.Inject

class CommunityRemoteDataSourceImpl
@Inject
constructor(
    private val communityAPI: CommunityAPI,
) : CommunityRemoteDataSource {

    override suspend fun savePhoto(
        priority: Long,
        photo: MultipartBody.Part
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun findPhotoById(
        photoId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun deletePhoto(
        photoId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun addPhotoToPostById(
        photoId: Long,
        postId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun savePost(
        title: String,
        content: String,
        author: String,
        photoIdList: List<Long>
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun findPostById(
        postId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun deletePostById(
        postId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun getPostsListByPageNum(
        pageNum: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun raiseLikeWithPostId(
        postId: Long,
        userId: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun decreaseLikeWithPostId(
        postId: Long,
        userId: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun saveComment(
        author: String,
        content: String,
        postId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun findCommentById(
        commentId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCommentById(
        commentId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCommentsByPostId(
        postId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun saveNestedComment(
        author: String,
        content: String,
        commentId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun findNestedCommentById(
        nestedCommentId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNestedCommentById(
        nestedCommentId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun getAllNestedCommentsByPostCommentId(
        commentId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

}