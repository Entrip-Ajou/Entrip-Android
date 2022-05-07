package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun findUserId(user_id: String): Flow<BaseResult<Boolean, Failure>>
    fun checkNickName(nickName: String) : Flow<BaseResult<Boolean, Failure>>
    fun insertNickName(user_id: String, nickName: String) : Flow<BaseResult<Int, Failure>>
}