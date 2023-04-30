package ajou.paran.domain.usecase

import ajou.paran.domain.repository.UserRepository
import javax.inject.Inject

class SignInByUserAccountUseCase
@Inject
constructor(
    private val repository: UserRepository,
) {

    suspend operator fun invoke(
        params: Params
    ) = kotlin.runCatching {
        repository.signInByUserAccount(
            userId = params.userId,
            password = params.password,
        )
    }

    data class Params(
        val userId: String,
        val password: String,
    )

}