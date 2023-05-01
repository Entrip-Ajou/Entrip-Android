package ajou.paran.domain.usecase

import ajou.paran.domain.repository.UserRepository
import javax.inject.Inject

class SaveIsEntryUseCase
@Inject
constructor(
    private val repository: UserRepository,
) {

    suspend operator fun invoke(
        params: Params = Params()
    ) = kotlin.runCatching {
        repository.saveIsEntry(isEntry = params.isEntry)
    }

    data class Params(
        val isEntry: Boolean = true
    )

}