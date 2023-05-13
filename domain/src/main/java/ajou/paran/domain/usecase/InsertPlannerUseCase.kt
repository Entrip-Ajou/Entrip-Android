package ajou.paran.domain.usecase

import ajou.paran.domain.model.BasePlanner
import ajou.paran.domain.repository.PlannerRepository
import javax.inject.Inject

class InsertPlannerUseCase
@Inject
constructor(
    private val repository: PlannerRepository
) {

    suspend operator fun invoke(
        params: Params
    ) = kotlin.runCatching {
        repository.insertPlanner(
            BasePlanner(
                id = params.id,
                title = params.title,
                startDate = params.startDate,
                endDate = params.endDate,
                timeStamp = params.timeStamp,
                commentTimeStamp = params.commentTimeStamp
            )
        )
    }

    data class Params(
        val id: Long,
        val title: String,
        val startDate: String,
        val endDate: String,
        val timeStamp: String,
        val commentTimeStamp: String
    )

}