package ajou.paran.entrip.repository.network

import ajou.paran.entrip.repository.network.api.CommunityApi
import ajou.paran.entrip.repository.network.dto.community.*
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import android.util.Log
import okhttp3.MultipartBody
import retrofit2.HttpException
import javax.inject.Inject

class CommunityRemoteSource
@Inject
constructor(
    private val communityApi: CommunityApi
) {
    companion object {
        private const val TAG = "[CommunityRS]"
    }

    suspend fun savePhoto(
        priority: Long = 1,
        image: MultipartBody.Part
    ): BaseResult<Long, Failure>
    = try {
        val response = communityApi.savePhoto(priority, image)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        e.run {
            when(code()) {
                503 -> {
                    response()?.let { response ->
                        response.errorBody()?.let { errorBody ->
                            Log.d(TAG, "error: ${errorBody.string()}")
                        }
                    }
                }
                else -> {}
            }
        }
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun findByIdPhoto(
        photoId: Long
    ): BaseResult<ResponseFindByIdPhoto, Failure>
    = try {
        when (photoId) {
            -1L -> {
                BaseResult.Success(data = ResponseFindByIdPhoto(-1L))
            }
            else -> {
                val response = communityApi.findByIdPhoto(photoId)
                when (response.status) {
                    200 -> {
                        BaseResult.Success(data = response.data)
                    }
                    else -> {
                        Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                        BaseResult.Error(Failure(response.status, response.message))
                    }
                }
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun deletePhoto(
        photoId: Long
    ): BaseResult<Long, Failure>
    = try {
        val response = communityApi.deletePhoto(photoId)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun addPostsToPhotos(
        photoId: Long,
        postId: Long
    ): BaseResult<Boolean, Failure>
    = try {
        val response = communityApi.addPostsToPhotos(photoId, postId)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun savePost(
        requestPost: RequestPost
    ): BaseResult<Long, Failure>
    = try {
        val response = communityApi.savePost(requestPost)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun findByIdPost(
       postId: Long
    ): BaseResult<ResponsePost, Failure>
    = try {
        val response = communityApi.findByIdPost(postId)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun deletePost(
        postId: Long
    ): BaseResult<Long, Failure>
    = try {
        val response = communityApi.deletePost(postId)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun getPostsListWithPageNum(
        pageNum: Long
    ): BaseResult<List<ResponsePost>, Failure>
    = try {
        val response = communityApi.getPostsListWithPageNum(pageNum)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun raiseLikeWithPostId(
        postId: Long,
        user_id: String
    ): BaseResult<Long, Failure>
    = try {
        val response = communityApi.raiseLikeWithPostId(postId, user_id)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun decreaseLikeWithPostId(
        postId: Long,
        user_id: String
    ): BaseResult<Long, Failure>
    = try {
        val response = communityApi.decreaseLikeWithPostId(postId, user_id)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun saveComment(
        requestComment: RequestComment
    ): BaseResult<Long, Failure>
    = try {
        val response = communityApi.saveComment(requestComment)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun findByIdComment(
        commentId: Long
    ): BaseResult<ResponseComment, Failure>
    = try {
        val response = communityApi.findByIdComment(commentId)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun deleteComment(
        commentId: Long
    ): BaseResult<Long, Failure>
    = try {
        val response = communityApi.deleteComment(commentId)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun getAllCommentsWithPostId(
        postId: Long
    ): BaseResult<List<ResponseComment>, Failure>
    = try {
        val response = communityApi.getAllCommentsWithPostId(postId)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun saveNestedComment(
        requestNestedComment: RequestNestedComment
    ): BaseResult<Long, Failure>
    = try {
        val response = communityApi.saveNestedComment(requestNestedComment)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun findByIdNestedComment(
        nestedCommentId: Long
    ): BaseResult<ResponseNestedComment, Failure>
    = try {
        val response = communityApi.findByIdNestedComment(nestedCommentId)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun deleteNestedComment(
        nestedCommentId: Long
    ): BaseResult<Long, Failure>
    = try {
        val response = communityApi.deleteNestedComment(nestedCommentId)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun getAllNestedCommentsWithPostCommentId(
        commentId: Long
    ): BaseResult<List<ResponseNestedComment>, Failure>
    = try {
        val response = communityApi.getAllNestedCommentsWithPostCommentId(commentId)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

}