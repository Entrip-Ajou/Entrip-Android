package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.dto.response.UserSaveResponseDto

interface UserRepository {

    suspend fun saveUserAccount(
        userId: String,
        nickname: String,
        photoUrl: String,
        token: String,
        gender: Long,
        password: String,
        attempt: Long = 0L
    ): UserSaveResponseDto

    suspend fun loginUserAccount(
        userId: String,
        password: String,
        attempt: Long = 0L
    ): Boolean

    suspend fun isExistUserId(
        userId: String,
        attempt: Long = 0L
    ): Boolean

    suspend fun isExistNickname(
        nickname: String,
        attempt: Long = 0L
    ): Boolean

}