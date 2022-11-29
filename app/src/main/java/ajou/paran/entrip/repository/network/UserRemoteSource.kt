package ajou.paran.entrip.repository.network

import ajou.paran.entrip.repository.network.api.UserAPIV2
import ajou.paran.entrip.repository.network.api.UserApi
import ajou.paran.entrip.repository.network.dto.*
import ajou.paran.entrip.repository.network.dto.request.UserLoginRequestDto
import ajou.paran.entrip.repository.network.dto.request.UserSaveRequestDto
import ajou.paran.entrip.repository.network.dto.response.UserLoginResponseDto
import ajou.paran.entrip.repository.network.dto.response.UserReissueAccessTokenResponseDto
import ajou.paran.entrip.repository.network.dto.response.UserSaveResponseDto
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import android.util.Log
import retrofit2.HttpException
import javax.inject.Inject

class UserRemoteSource
@Inject
constructor(
    private val userApi: UserApi,
    private val userAPIV2: UserAPIV2
) {
    companion object {
        const val TAG = "[UserRemote]"
    }

    suspend fun saveUser(user: UserTemp): BaseResponse<UserRequest>
    = userApi.saveUser(user)

    suspend fun updateUserToken(user_id: String, token: String): BaseResult<Unit, Failure>
    = try {
        val res = userApi.updateToken(user_id, token)
        when(res.status) {
            200 -> { BaseResult.Success(Unit) }
            else -> {
                Log.e(CommentRemoteSource.TAG, "Err code = ${res.status}, Err message = ${res.message}")
                BaseResult.Error(Failure(res.status, res.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(CommentRemoteSource.TAG, "NoInternetException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(CommentRemoteSource.TAG, "HttpException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(e.code(), e.message.toString()))
    } catch (e: Exception) {
        Log.e(CommentRemoteSource.TAG, "Exception Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun getUserPlanners(user_id: String): BaseResponse<List<PlannerResponse>>
    = userApi.findAllPlanners(user_id)

    suspend fun findById(user_id: String): BaseResponse<UserResponse>
    = userApi.findById(user_id)

    suspend fun findByUserId(user_id: String): BaseResponse<UserResponse>
    = userApi.findByUserId(user_id)

    suspend fun saveUserAccount(
        userSaveRequest: UserSaveRequestDto
    ): BaseResult<UserSaveResponseDto, Failure> = try {
        val response = userAPIV2.saveUserAccount(saveRequest = userSaveRequest)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            202 -> {
                // exist account
                Log.d(TAG, "Networking Message = ${response.message}")
                BaseResult.Error(Failure(response.status, response.message))
            }
            else -> {
                Log.d(TAG, "Networking Message = ${response.message}")
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

    suspend fun loginUserAccount(
        loginRequest: UserLoginRequestDto
    ): BaseResult<UserLoginResponseDto, Failure> = try {
        val response = userAPIV2.loginUserAccount(loginRequest = loginRequest)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
            }
            202 -> {
                BaseResult.Error(Failure(response.status, response.message))
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

//    suspend fun reissueUserAccessToken(
//        refreshToken: String
//    ): BaseResult<UserReissueAccessTokenResponseDto, Failure> = try {
//        val response = userAPIV2.reissueUserAccessToken(refreshToken = refreshToken)
//        when (response.status) {
//            200 -> {
//                BaseResult.Success(data = response.data)
//            }
//            else -> {
//                Log.e(TAG, "Networking Message = ${response.message}")
//                BaseResult.Error(Failure(response.status, response.message))
//            }
//        }
//    } catch (e: NoInternetException) {
//        Log.e(TAG, "NoInternetException Message = ${e.localizedMessage}")
//        BaseResult.Error(Failure(0, e.message))
//    } catch (e: HttpException) {
//        Log.e(TAG, "HttpException Message = ${e.localizedMessage}")
//        BaseResult.Error(Failure(e.code(), e.message()))
//    } catch (e: Exception) {
//        Log.e(TAG, "Exception Message = ${e.localizedMessage}")
//        BaseResult.Error(Failure(-1, e.message.toString()))
//    }

    suspend fun isExistUserId(
        userId: String
    ): BaseResult<Boolean, Failure> = try {
        val response = userAPIV2.isExistUserId(userId = userId)
        when (response.status) {
            200 -> {
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

    suspend fun isExistNickname(
        nickname: String
    ): BaseResult<Boolean, Failure> = try {
        val response = userAPIV2.isExistNickname(nickname = nickname)
        when (response.status) {
            200 -> {
                BaseResult.Success(response.data)
            }
            202 -> {
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