package ajou.paran.entrip.repository.network

import ajou.paran.entrip.repository.network.api.CommentApi
import ajou.paran.entrip.repository.network.api.TokenApi
import ajou.paran.entrip.repository.network.dto.CommentPlanResponse
import ajou.paran.entrip.repository.network.dto.CommentRequest
import ajou.paran.entrip.repository.network.dto.CommentResponse
import ajou.paran.entrip.repository.network.dto.response.UserReissueAccessTokenResponseDto
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import android.content.SharedPreferences
import android.util.Log
import retrofit2.HttpException
import javax.inject.Inject

class CommentRemoteSource
@Inject
constructor(
    private val commentApi: CommentApi,
    private val tokenApi: TokenApi,
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val TAG = "[CommentRemote]"
    }

    suspend fun insertComment(
        commentRequest: CommentRequest
    ): BaseResult<CommentPlanResponse, Failure> = try {
        val response = commentApi.insertComment(commentRequest)
        when (response.status) {
            200 -> {
                BaseResult.Success(response.data)
            }
            else -> {
                Log.e(TAG, "Err code = "+response.status+ " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        when (e.code()) {
            400 -> {
                when (val response = reissueUserAccessToken()) {
                    is BaseResult.Success -> {
                        insertComment(commentRequest)
                    }
                    is BaseResult.Error -> {
                        BaseResult.Error(response.err)
                    }
                }
            }
            else -> {
                BaseResult.Error(Failure(e.code(), e.message()))
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun deleteComment(
        comment_id: Long
    ): BaseResult<CommentPlanResponse, Failure> = try {
        val response = commentApi.deleteComment(comment_id)
        when (response.status) {
            200 -> {
                BaseResult.Success(response.data)
            }
            else -> {
                Log.e(TAG, "Err code = "+response.status+ " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        when (e.code()) {
            400 -> {
                when (val response = reissueUserAccessToken()) {
                    is BaseResult.Success -> {
                        deleteComment(comment_id)
                    }
                    is BaseResult.Error -> {
                        BaseResult.Error(response.err)
                    }
                }
            }
            else -> {
                BaseResult.Error(Failure(e.code(), e.message()))
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun selectComment(
        plan_id: Long
    ): BaseResult<List<CommentResponse>, Failure> = try {
        val response = commentApi.selectComment(plan_id)
        when (response.status) {
            200 -> {
                val comments = mutableListOf<CommentResponse>()
                response.data.forEach { t ->
                    comments.add(
                        CommentResponse(
                            comment_id = t.comment_id,
                            user_id = t.user_id,
                            content = t.content,
                            photoUrl = t.photoUrl,
                            nickname = t.nickname
                        )
                    )
                }
                BaseResult.Success(comments)
            }
            else -> {
                Log.e(TAG, "Err code = "+response.status+ " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        when (e.code()) {
            400 -> {
                when (val response = reissueUserAccessToken()) {
                    is BaseResult.Success -> {
                        selectComment(plan_id)
                    }
                    is BaseResult.Error -> {
                        BaseResult.Error(response.err)
                    }
                }
            }
            else -> {
                BaseResult.Error(Failure(e.code(), e.message()))
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    private suspend fun reissueUserAccessToken(
        refreshToken: String = sharedPreferences.getString("refreshToken", "").toString()
    ): BaseResult<String, Failure> = try {
        val response = tokenApi.reissueUserAccessToken(refreshToken = refreshToken)
        when (response.status) {
            200 -> {
                sharedPreferences.edit().putString("accessToken", response.data).commit()
                BaseResult.Success(response.data)
            }
            else -> {
                Log.e(TAG, "Networking Message = ${response.message}")
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(-1, e.message.toString()))
    }
}