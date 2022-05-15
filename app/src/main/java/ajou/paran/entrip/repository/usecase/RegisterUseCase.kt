package ajou.paran.entrip.repository.usecase

import ajou.paran.entrip.repository.Impl.UserRepository
import ajou.paran.entrip.repository.network.dto.UserRequest
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class IsExistUserUseCase
@Inject
constructor(
    private val userRepository: UserRepository
){
    fun execute(user_id: String): Flow<BaseResult<Boolean, Failure>> = userRepository.isExistUserId(user_id).flowOn(Dispatchers.IO)
}

class IsExistNicknameUseCase
@Inject
constructor(
    private val userRepository: UserRepository
){
    fun execute(nickname: String): Flow<BaseResult<Boolean, Failure>> = userRepository.isExistNickname(nickname).flowOn(Dispatchers.IO)
}

class IsSaveUserUseCase
@Inject
constructor(
    private val userRepository: UserRepository
){
    fun execute(user_id: String, gender: Int, nickName: String): Flow<BaseResult<UserRequest, Failure>> = userRepository.saveUser(user_id, gender, nickName).flowOn(Dispatchers.IO)
}