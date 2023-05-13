package ajou.paran.domain.usecase

import ajou.paran.domain.repository.PlanRepository
import javax.inject.Inject

class SelectPlanByIdWithDateUseCase
@Inject
constructor(
    private val repository: PlanRepository
) {

    suspend operator fun invoke(
        params: Params
    ) = kotlin.runCatching {
        repository.selectPlanByIdWithDate(
            planDate = params.planDate,
            plannerId = params.plannerId
        )
    }

    data class Params(
        val planDate: String,
        val plannerId: Long
    )

}