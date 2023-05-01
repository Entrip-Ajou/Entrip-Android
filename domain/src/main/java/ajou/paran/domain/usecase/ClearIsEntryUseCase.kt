package ajou.paran.domain.usecase

import ajou.paran.domain.repository.UserRepository
import javax.inject.Inject

class ClearIsEntryUseCase
@Inject
constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke() = kotlin.runCatching {
        repository.clearIsEntry()
    }

}