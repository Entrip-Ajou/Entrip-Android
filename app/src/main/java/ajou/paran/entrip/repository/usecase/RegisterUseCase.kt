package ajou.paran.entrip.repository.usecase

import ajou.paran.entrip.repository.Impl.UserRepository
import javax.inject.Inject

class IsExistUserUseCase
@Inject
constructor(
    private val userRepository: UserRepository
){
    fun <T> execute(user_id: String) = userRepository.findUserId(user_id)
}

class IsExistNickNameUseCase
@Inject
constructor(
    private val userRepository: UserRepository
){

    fun <T> execute(nickName: String) = userRepository.checkNickName(nickName)
}

class IsInsertUserUseCase
@Inject
constructor(
    private val userRepository: UserRepository
){
    fun <T> execute(user_id: String, nickName: String) = userRepository.insertNickName(user_id, nickName)
}