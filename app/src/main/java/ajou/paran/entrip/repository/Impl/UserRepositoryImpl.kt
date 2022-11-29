package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.UserRemoteSource
import ajou.paran.entrip.repository.network.dto.request.UserLoginRequestDto
import ajou.paran.entrip.repository.network.dto.request.UserSaveRequestDto
import ajou.paran.entrip.repository.network.dto.response.UserSaveResponseDto
import ajou.paran.entrip.util.network.BaseResult
import android.content.SharedPreferences
import android.util.Log
import javax.inject.Inject

class UserRepositoryImpl
@Inject
constructor(
    private val userRemoteSource: UserRemoteSource,
    private val sharedPreferences: SharedPreferences
) : UserRepository {

    companion object {
        const val TAG = "[UserImpl]"
    }

    override suspend fun saveUserAccount(
        userId: String,
        nickname: String,
        photoUrl: String,
        token: String,
        gender: Long,
        password: String,
        attempt: Long
    ): UserSaveResponseDto = when (val response = userRemoteSource.saveUserAccount(
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
                    when (attempt) {
                        in 0 .. 2 -> {
                            saveUserAccount(userId, nickname, photoUrl, token, gender, password)
                        }
                        else -> {
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
                202 -> {
                    Log.d(TAG, "An account that already exists")
                    UserSaveResponseDto(
                        userId = "",
                        nickname = "",
                        gender = -1L,
                        photoUrl = "",
                        token = ""
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
    ): Boolean = when (val response = userRemoteSource.loginUserAccount(loginRequest = UserLoginRequestDto(userId = userId, password = password))) {
        is BaseResult.Success -> {
            sharedPreferences.edit().putString("user_id", response.data.userId).commit()
            sharedPreferences.edit().putString("nickname", response.data.nickname).commit()
            sharedPreferences.edit().putString("accessToken", response.data.accessToken).commit()
            sharedPreferences.edit().putString("refreshToken", response.data.refreshToken).commit()
            true
        }
        is BaseResult.Error -> {
            when(response.err.code) {
                0 -> {
                    Log.d(TAG, "Internet Error")
                    when (attempt) {
                        in 0 .. 2 -> {
                            loginUserAccount(userId, password, attempt + 1)
                        }
                        else -> {
                            false
                        }
                    }
                }
                else -> {
                    Log.d(TAG, "Error code = ${response.err.code}, message = ${response.err.message}")
                    false
                }
            }
        }
    }

    override suspend fun isExistUserId(
        userId: String,
        attempt: Long
    ): Boolean = when (val response = userRemoteSource.isExistUserId(userId = userId)) {
        is BaseResult.Success -> {
            response.data
        }
        is BaseResult.Error -> {
            when(response.err.code) {
                0 -> {
                    Log.d(TAG, "Internet Error")
                    when (attempt) {
                        in 0 .. 2 -> {
                            isExistUserId(userId, attempt + 1)
                        }
                        else -> {
                            false
                        }
                    }
                }
                else -> {
                    Log.d(TAG, "Error code = ${response.err.code}, message = ${response.err.message}")
                    false
                }
            }
        }
    }

    override suspend fun isExistNickname(
        nickname: String,
        attempt: Long
    ): Boolean = when (val response = userRemoteSource.isExistNickname(nickname = nickname)) {
        is BaseResult.Success -> {
            response.data
        }
        is BaseResult.Error -> {
            when(response.err.code) {
                0 -> {
                    Log.d(TAG, "Internet Error")
                    when (attempt) {
                        in 0 .. 2 -> {
                            isExistNickname(nickname, attempt + 1)
                        }
                        else -> {
                            true
                        }
                    }
                }
                else -> {
                    Log.d(TAG, "Error code = ${response.err.code}, message = ${response.err.message}")
                    true
                }
            }
        }
    }

}