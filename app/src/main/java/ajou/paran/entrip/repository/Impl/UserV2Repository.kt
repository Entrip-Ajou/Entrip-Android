package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.dto.response.UserLoginResponseDto
import ajou.paran.entrip.repository.network.dto.response.UserSaveResponseDto

interface UserV2Repository {

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

    suspend fun reissueUserAccessToken(
        refreshToken: String,
        attempt: Long = 0L
    ): Boolean

}