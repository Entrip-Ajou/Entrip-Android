package ajou.paran.domain.usecase

import ajou.paran.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserAccountUseCase
@Inject
constructor(
    private val repository: UserRepository,
) {

    suspend operator fun invoke(
        params: Params
    ) = kotlin.runCatching {
        repository.saveUserAccount(
            userId = params.userId,
            nickname = params.nickname,
            photoUrl = params.photoUrl,
            gender = params.gender,
            password = params.password
        )
    }

    data class Params(
        val userId: String,
        val nickname: String,
        val photoUrl: String,
        val gender: Long,
        val password: String,
    )

}