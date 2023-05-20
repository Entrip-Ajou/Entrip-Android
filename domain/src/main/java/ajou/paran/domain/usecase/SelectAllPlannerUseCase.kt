package ajou.paran.domain.usecase

import ajou.paran.domain.repository.PlannerRepository
import javax.inject.Inject

class SelectAllPlannerUseCase
@Inject
constructor(
    private val repository: PlannerRepository
) {

    suspend operator fun invoke() = kotlin.runCatching {
        repository.selectAllPlanner()
    }

}