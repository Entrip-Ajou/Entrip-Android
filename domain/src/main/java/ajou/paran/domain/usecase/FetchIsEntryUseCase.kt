package ajou.paran.domain.usecase

import ajou.paran.domain.repository.UserRepository
import javax.inject.Inject

class FetchIsEntryUseCase
@Inject
constructor(
    private val repository: UserRepository,
) {

    suspend operator fun invoke(): Boolean = kotlin.runCatching {
        repository.fetchIsEntry()
    }.getOrDefault(false)

}