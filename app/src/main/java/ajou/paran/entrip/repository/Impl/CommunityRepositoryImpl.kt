package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.CommunityRemoteSource
import ajou.paran.entrip.repository.network.dto.community.*
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import okhttp3.MultipartBody
import javax.inject.Inject

class CommunityRepositoryImpl
@Inject
constructor(
    private val communityRemoteSource: CommunityRemoteSource
) : CommunityRepository {

    override suspend fun savePhoto(
        priority: Long,
        image: MultipartBody.Part,
        attempt: Int
    ): Long
    = when (val result = communityRemoteSource.savePhoto(priority, image)) {
        is BaseResult.Success -> {
            result.data
        }
        is BaseResult.Error -> {
            if(attempt >= 3) -1 else savePhoto(priority, image, attempt + 1)
        }
    }


    override suspend fun findByIdPhoto(
        photoId: Long,
        attempt: Int
    ): ResponseFindByIdPhoto
    = when (val result = communityRemoteSource.findByIdPhoto(photoId)) {
        is BaseResult.Success -> {
            result.data
        }
        is BaseResult.Error -> {
            when (result.err.code) {
                202 -> {
                    ResponseFindByIdPhoto(photoId = -1L)
                }
                else -> {
                    if(attempt >= 3) {
                        ResponseFindByIdPhoto(photoId = -1L)
                    } else {
                        findByIdPhoto(photoId, attempt + 1)
                    }
                }
            }

        }
    }

    override suspend fun deletePhoto(photoId: Long): BaseResult<Long, Failure> {
        TODO("Not yet implemented")
    }

    override suspend fun addPostsToPhotos(
        photoId: Long,
        postId: Long
    ): BaseResult<Boolean, Failure> {
        TODO("Not yet implemented")
    }

    override suspend fun savePost(requestPost: RequestPost, attempt: Int): Long
    = when(val result = communityRemoteSource.savePost(requestPost)) {
        is BaseResult.Success -> {
            result.data
        }
        is BaseResult.Error -> {
            if(attempt >= 3)
                -1
            else
                savePost(requestPost, attempt + 1)
        }
    }


    override suspend fun findByIdPost(postId: Long, attempt: Int): ResponsePost
    = when(val result = communityRemoteSource.findByIdPost(postId)) {
        is BaseResult.Success -> {
            result.data
        }
        is BaseResult.Error -> {
            if(attempt >= 3)
                ResponsePost(
                    post_id = -1L,
                    title = "",
                    content = "",
                    author = "",
                    likeNumber = 0,
                    commentsNumber = 0,
                    photoList = listOf()
                )
            else
                findByIdPost(postId, attempt + 1)
        }
    }

    override suspend fun deletePost(postId: Long): BaseResult<Long, Failure> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostsListWithPageNum(pageNum: Long): List<ResponsePost>
    = when (val boardList = communityRemoteSource.getPostsListWithPageNum(pageNum)) {
        is BaseResult.Success -> {
            boardList.data
        }
        is BaseResult.Error -> {
            listOf()
        }
    }


    override suspend fun raiseLikeWithPostId(
        postId: Long,
        user_id: String
    ): BaseResult<Long, Failure> {
        TODO("Not yet implemented")
    }

    override suspend fun decreaseLikeWithPostId(
        postId: Long,
        user_id: String
    ): BaseResult<Long, Failure> {
        TODO("Not yet implemented")
    }

    override suspend fun saveComment(
        author: String,
        content: String,
        postId: Long,
        attempt: Int
    ): Long = when (val result = communityRemoteSource.saveComment(
            RequestComment(
                author = author,
                content = content,
                postId = postId
            )
        )
    ) {
        is BaseResult.Success -> {
            result.data
        }
        is BaseResult.Error -> {
            if (attempt >= 3) {
                -1L
            } else {
                saveComment(
                    author = author,
                    content = content,
                    postId = postId,
                    attempt = attempt + 1
                )
            }
        }
    }


    override suspend fun findByIdComment(
        commentId: Long
    ): ResponseComment = when (val result = communityRemoteSource.findByIdComment(commentId)) {
        is BaseResult.Success -> {
            result.data
        }
        is BaseResult.Error -> {
            ResponseComment(commentId = -1L)
        }
    }

    override suspend fun deleteComment(
        commentId: Long
    ): Long = when (val result = communityRemoteSource.deleteComment(commentId)) {
        is BaseResult.Success -> {
            result.data
        }
        is BaseResult.Error -> {
            -1L
        }
    }

    override suspend fun getAllCommentsWithPostId(
        postId: Long,
        attempt: Int
    ): List<ResponseComment> = when (val result = communityRemoteSource.getAllCommentsWithPostId(postId)) {
        is BaseResult.Success -> {
            result.data
        }
        is BaseResult.Error -> {
            if (attempt > 3) {
                listOf()
            } else {
                getAllCommentsWithPostId(
                    postId = postId,
                    attempt = attempt + 1
                )
            }
        }
    }

    override suspend fun saveNestedComment(
        author: String,
        content: String,
        commentId: Long,
        attempt: Int
    ): Long = when (val result = communityRemoteSource.saveNestedComment(
        RequestNestedComment(
            author = author,
            content = content,
            commentId = commentId
        )
    )) {
        is BaseResult.Success -> {
            result.data
        }
        is BaseResult.Error -> {
            if (attempt > 3) {
                -1L
            } else {
                saveNestedComment(
                    author = author,
                    content = content,
                    commentId = commentId,
                    attempt = attempt + 1
                )
            }
        }
    }

    override suspend fun findByIdNestedComment(
        nestedCommentId: Long
    ): ResponseNestedComment = when (val result = communityRemoteSource.findByIdNestedComment(nestedCommentId)) {
        is BaseResult.Success -> {
            result.data
        }
        is BaseResult.Error -> {
            ResponseNestedComment(nestedCommentId = -1L)
        }
    }

    override suspend fun deleteNestedComment(
        nestedCommentId: Long
    ): Long = when (val result = communityRemoteSource.deleteNestedComment(nestedCommentId)) {
        is BaseResult.Success -> {
            result.data
        }
        is BaseResult.Error -> {
            -1L
        }
    }

    override suspend fun getAllNestedCommentsWithPostCommentId(
        commentId: Long,
        attempt: Int
    ): List<ResponseNestedComment> = when (val result = communityRemoteSource.getAllNestedCommentsWithPostCommentId(commentId)) {
        is BaseResult.Success -> {
            result.data
        }
        is BaseResult.Error -> {
            if (attempt > 3) {
                listOf()
            } else {
                getAllNestedCommentsWithPostCommentId(
                    commentId = commentId,
                    attempt = attempt + 1
                )
            }
        }
    }

}