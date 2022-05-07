package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.UserRemoteSource
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl
@Inject
constructor(
    private val userRemoteSource: UserRemoteSource
): UserRepository{
    override fun findUserId(user_id: String): Flow<BaseResult<Boolean, Failure>>
     = flow {
        try {
            val response = userRemoteSource.findUserId(user_id)
            if (response.httpStatus == 200) {
                val dto = response.data
                emit(BaseResult.Success(dto))
            } else {
                emit(BaseResult.Error(Failure(response.httpStatus, response.message)))
            }
        } catch (e: NoInternetException) {
            emit(BaseResult.Error(Failure(0, e.message)))
        } catch (e: Exception) {
            emit(BaseResult.Error(Failure(-1, e.message.toString())))
        }
    }

    override fun checkNickName(nickName: String) : Flow<BaseResult<Boolean, Failure>>
     = flow{
        try {
            val response = userRemoteSource.checkNickName(nickName)
            if (response.httpStatus == 200) {
                val dto = response.data
                emit(BaseResult.Success(dto))
            } else {
                emit(BaseResult.Error(Failure(response.httpStatus, response.message)))
            }
        } catch (e: NoInternetException) {
            emit(BaseResult.Error(Failure(0, e.message)))
        } catch (e: Exception) {
            emit(BaseResult.Error(Failure(-1, e.message.toString())))
        }
    }

    override fun insertNickName(user_id: String, nickName: String) : Flow<BaseResult<Int, Failure>>
     = flow {
        try {
            val response = userRemoteSource.insertNickName(user_id, nickName)
            if (response.httpStatus == 200) {
                val dto = response.data
                emit(BaseResult.Success(dto))
            } else {
                emit(BaseResult.Error(Failure(response.httpStatus, response.message)))
            }
        } catch (e: NoInternetException) {
            emit(BaseResult.Error(Failure(0, e.message)))
        } catch (e: Exception) {
            emit(BaseResult.Error(Failure(-1, e.message.toString())))
        }
    }
}