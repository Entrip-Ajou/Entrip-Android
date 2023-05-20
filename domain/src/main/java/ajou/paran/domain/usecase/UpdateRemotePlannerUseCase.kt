package ajou.paran.domain.usecase

import ajou.paran.domain.repository.PlannerRepository
import javax.inject.Inject

class UpdateRemotePlannerUseCase
@Inject
constructor(
    private val repository: PlannerRepository
) {

    suspend operator fun invoke(params: Params) = kotlin.runCatching {
        repository.updateRemotePlanner(
            plannerId = params.plannerId,
            title = params.title,
            startDate = params.startDate,
            endDate = params.endDate
        )
    }

    data class Params(
        val plannerId: Long,
        val title: String,
        val startDate: String,
        val endDate: String
    )
}