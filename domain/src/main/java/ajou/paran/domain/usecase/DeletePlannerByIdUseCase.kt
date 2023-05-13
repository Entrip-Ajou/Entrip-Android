package ajou.paran.domain.usecase

import ajou.paran.domain.repository.PlannerRepository
import javax.inject.Inject

class DeletePlannerByIdUseCase
@Inject
constructor(
    private val repository: PlannerRepository
) {

    suspend operator fun invoke(
        plannerId: Long
    ) = kotlin.runCatching {
        repository.deletePlannerById(plannerId = plannerId)
    }

}