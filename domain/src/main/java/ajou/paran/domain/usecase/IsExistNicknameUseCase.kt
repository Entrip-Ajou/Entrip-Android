package ajou.paran.domain.usecase

import ajou.paran.domain.repository.UserRepository
import javax.inject.Inject

class IsExistNicknameUseCase
@Inject
constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(
        params: Params
    ): Boolean = kotlin.runCatching {
        repository.isExistNickname(
            nickname = params.nickname
        )
    }.getOrDefault(false)

    data class Params(
        val nickname: String
    )
}