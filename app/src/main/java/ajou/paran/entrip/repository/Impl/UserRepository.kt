package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.dto.response.UserSaveResponseDto
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure

interface UserRepository {

    suspend fun saveUserAccount(
        userId: String,
        nickname: String,
        photoUrl: String,
        token: String,
        gender: Long,
        password: String,
        attempt: Long = 0L
    ): BaseResult<UserSaveResponseDto, Failure>

    suspend fun loginUserAccount(
        userId: String,
        password: String,
        attempt: Long = 0L
    ): BaseResult<Boolean, Failure>

    suspend fun isExistUserId(
        userId: String,
        attempt: Long = 0L
    ): BaseResult<Boolean, Failure>

    suspend fun isExistNickname(
        nickname: String,
        attempt: Long = 0L
    ): BaseResult<Boolean, Failure>

}