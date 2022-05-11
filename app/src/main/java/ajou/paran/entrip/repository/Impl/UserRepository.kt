package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun isExistUserId(user_id: String): Flow<BaseResult<Boolean, Failure>>
    fun isExistNickname(nickName: String) : Flow<BaseResult<Boolean, Failure>>
    fun saveUser(user_id: String, gender: Int, nickName: String) : Flow<BaseResult<Boolean, Failure>>
}