package ajou.paran.data.repository

import ajou.paran.data.local.datasource.PlannerRoomDataSource
import ajou.paran.data.remote.datasource.PlanRemoteDataSource
import ajou.paran.domain.model.BasePlan
import ajou.paran.domain.repository.PlanRepository
import javax.inject.Inject

class PlanRepositoryImpl
@Inject
constructor(
    private val plannerRoomDataSource: PlannerRoomDataSource,
    private val planRemoteDataSource: PlanRemoteDataSource
) : PlanRepository {

    override suspend fun addPlan(
        plannerId: Long,
        date: String,
        todo: String,
        time: Int,
        location: String?
    ) {
        planRemoteDataSource.addPlan(
            plannerId = plannerId,
            date = date,
            todo = todo,
            time = time,
            location = location
        ).apply {
            insertPlan(this)
        }
    }

    override suspend fun insertPlan(plan: BasePlan) = plannerRoomDataSource.insertPlan(plan = plan)

    override suspend fun deletePlanById(planId: Long) = plannerRoomDataSource.deletePlanById(planId = planId)

    override suspend fun selectPlanByIdWithDate(
        planDate: String,
        plannerId: Long
    ): List<BasePlan> = selectPlanByIdWithDate(planDate, plannerId).apply {

    }

    override suspend fun insertAllPlan(planList: List<BasePlan>) = plannerRoomDataSource.insertAllPlan(planList)

    override suspend fun updatePlan(plan: BasePlan) = plannerRoomDataSource.updatePlan(plan)

}