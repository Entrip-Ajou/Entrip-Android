package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.dto.PlannerResponse
import ajou.paran.entrip.repository.network.dto.TripResponse
import ajou.paran.entrip.repository.network.dto.UserRequest
import ajou.paran.entrip.repository.network.dto.UserResponse
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserPlanners(user_id: String): Flow<BaseResult<List<PlannerResponse>, Failure>>
    fun isExistUserId(user_id: String): Flow<BaseResult<Boolean, Failure>>
    fun isExistNickname(nickName: String) : Flow<BaseResult<Boolean, Failure>>
    fun saveUser(user_id: String, gender: Int, nickName: String) : Flow<BaseResult<UserRequest, Failure>>
    fun findById(user_id : String):Flow<BaseResult<UserResponse, Failure>>
    fun getTrip(user_id: String): Flow<BaseResult<List<TripResponse>, Failure>>
}