package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.UserV2RemoteSource
import ajou.paran.entrip.repository.network.dto.request.UserLoginRequestDto
import ajou.paran.entrip.repository.network.dto.request.UserSaveRequestDto
import ajou.paran.entrip.repository.network.dto.response.UserSaveResponseDto
import ajou.paran.entrip.util.network.BaseResult
import android.content.SharedPreferences
import android.util.Log
import javax.inject.Inject

class UserV2RepositoryImpl
@Inject
constructor(
    private val userV2RS: UserV2RemoteSource,
    private val sharedPreferences: SharedPreferences
) : UserV2Repository {
    companion object {
        private const val TAG = "[UserRepo]"
    }

    override suspend fun saveUserAccount(
        userId: String,
        nickname: String,
        photoUrl: String,
        token: String,
        gender: Long,
        password: String,
        attempt: Long
    ): UserSaveResponseDto
    = when (val response = userV2RS.saveUserAccount(
            userSaveRequest = UserSaveRequestDto(
                userId = userId,
                nickname = nickname,
                photoUrl = photoUrl,
                token = token,
                gender = gender,
                password = password
            )
        )
    ) {
        is BaseResult.Success -> {
            response.data
        }
        is BaseResult.Error -> {
            when(response.err.code) {
                0 -> {
                    Log.d(TAG, "Internet Error")
                    if (attempt < 3)
                        saveUserAccount(userId, nickname, photoUrl, token, gender, password)
                    else
                        UserSaveResponseDto(
                            userId = userId,
                            nickname = nickname,
                            gender = gender,
                            photoUrl = photoUrl,
                            token = token
                        )
                }
                else -> {
                    Log.d(TAG, "Error code = ${response.err.code}, message = ${response.err.message}")
                    UserSaveResponseDto(
                        userId = userId,
                        nickname = nickname,
                        gender = gender,
                        photoUrl = photoUrl,
                        token = token
                    )
                }
            }
        }
    }


    override suspend fun loginUserAccount(
        userId: String,
        password: String,
        attempt: Long
    ): Boolean
    = when (val response = userV2RS.loginUserAccount(
            loginRequest = UserLoginRequestDto(
                userId = userId,
                password = password
            )
        )
    ) {
        is BaseResult.Success -> {
            sharedPreferences.edit().putString("user_id", response.data.userId).commit()
            sharedPreferences.edit().putString("user_id", response.data.nickname).commit()
            sharedPreferences.edit().putString("accessToken", response.data.accessToken).commit()
            sharedPreferences.edit().putString("refreshToken", response.data.refreshToken).commit()
            true
        }
        is BaseResult.Error -> {
            when(response.err.code) {
                0 -> {
                    Log.d(TAG, "Internet Error")
                    if (attempt < 3)
                        loginUserAccount(userId, password, attempt + 1)
                    else
                        false
                }
                else -> {
                    Log.d(TAG, "Error code = ${response.err.code}, message = ${response.err.message}")
                    false
                }
            }
        }
    }

    override suspend fun reissueUserAccessToken(
        refreshToken: String,
        attempt: Long
    ): Boolean
    = when (val response = userV2RS.reissueUserAccessToken(refreshToken = refreshToken)) {
        is BaseResult.Success -> {
            response.data
            true
        }
        is BaseResult.Error -> {
            when(response.err.code) {
                0 -> {
                    Log.d(TAG, "Internet Error")
                    if (attempt < 3)
                        reissueUserAccessToken(refreshToken, attempt + 1)
                    else
                        false
                }
                else -> {
                    Log.d(TAG, "Error code = ${response.err.code}, message = ${response.err.message}")
                    false
                }
            }
        }
    }

}