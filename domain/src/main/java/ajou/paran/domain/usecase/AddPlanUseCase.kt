package ajou.paran.domain.usecase

import ajou.paran.domain.repository.PlanRepository
import javax.inject.Inject

class AddPlanUseCase
@Inject
constructor(
    private val repository: PlanRepository
){

    suspend operator fun invoke(params: Params) = kotlin.runCatching {
        repository.addPlan(
            plannerId = params.plannerId,
            date = params.date,
            todo = params.todo,
            time = params.time,
            location = params.location,
            rgb = params.rgb,
        )
    }

    data class Params(
        val plannerId: Long,
        val date: String,
        val todo: String,
        val time: Int,
        val location: String?,
        val rgb: Long
    )

}