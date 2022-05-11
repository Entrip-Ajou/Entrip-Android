package ajou.paran.entrip.repository.usecase

import ajou.paran.entrip.repository.Impl.UserRepository
import javax.inject.Inject

class IsExistUserUseCase
@Inject
constructor(
    private val userRepository: UserRepository
){
    fun execute(user_id: String) = userRepository.isExistUserId(user_id)
}

class IsExistNicknameUseCase
@Inject
constructor(
    private val userRepository: UserRepository
){
    fun execute(nickname: String) = userRepository.isExistNickname(nickname)
}

class IsSaveUserUseCase
@Inject
constructor(
    private val userRepository: UserRepository
){
    fun execute(user_id: String, gender: Int, nickName: String) = userRepository.saveUser(user_id, gender, nickName)
}