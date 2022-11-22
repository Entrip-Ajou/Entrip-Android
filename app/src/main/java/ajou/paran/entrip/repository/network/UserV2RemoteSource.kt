package ajou.paran.entrip.repository.network

import ajou.paran.entrip.repository.network.api.UserAPIV2
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

class UserV2RemoteSource
@Inject
constructor(
    private val userAPIV2: UserAPIV2
) {
    companion object {
        private const val TAG = "[UserRS]"
    }

    suspend fun saveUserAccount(
        userSaveRequest: UserSaveRequestDto
    ): BaseResult<UserSaveResponseDto, Failure> = try {
        val response = userAPIV2.saveUserAccount(saveRequest = userSaveRequest)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
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

    suspend fun reissueUserAccessToken(
        refreshToken: String
    ): BaseResult<UserReissueAccessTokenResponseDto, Failure> = try {
        val response = userAPIV2.reissueUserAccessToken(refreshToken = refreshToken)
        when (response.status) {
            200 -> {
                BaseResult.Success(data = response.data)
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