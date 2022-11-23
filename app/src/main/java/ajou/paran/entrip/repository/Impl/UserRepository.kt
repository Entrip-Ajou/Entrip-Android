package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.dto.PlannerResponse
import ajou.paran.entrip.repository.network.dto.TripResponse
import ajou.paran.entrip.repository.network.dto.UserRequest
import ajou.paran.entrip.repository.network.dto.UserResponse
import ajou.paran.entrip.repository.network.dto.response.UserSaveResponseDto
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserPlanners(user_id: String): Flow<BaseResult<List<PlannerResponse>, Failure>>
    suspend fun getUserPlannersResult(user_id: String): BaseResult<List<PlannerResponse>, Failure>
    fun saveUser(user_id: String, gender: Int, nickName: String) : Flow<BaseResult<UserRequest, Failure>>
    fun findById(user_id : String):Flow<BaseResult<UserResponse, Failure>>

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

    suspend fun isExistUserId(
        userId: String,
        attempt: Long = 0L
    ): Boolean

    suspend fun isExistNickname(
        nickname: String,
        attempt: Long = 0L
    ): Boolean

}