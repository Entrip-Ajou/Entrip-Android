package ajou.paran.domain.usecase

import ajou.paran.domain.repository.UserRepository
import javax.inject.Inject

class FindAllPlannersByUserUseCase
@Inject
constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(
        userId: String,
    ) = kotlin.runCatching {
        repository.findAllPlannersByUser(userId)
    }

}