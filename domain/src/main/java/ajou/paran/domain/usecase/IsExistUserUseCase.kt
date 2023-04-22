package ajou.paran.domain.usecase

import ajou.paran.domain.repository.UserRepository
import javax.inject.Inject

class IsExistUserUseCase
@Inject
constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(
        params: Params
    ) = kotlin.runCatching {
        repository.isExistUser(
            userId = params.userId
        )
    }

    data class Params(
        val userId: String
    )
}