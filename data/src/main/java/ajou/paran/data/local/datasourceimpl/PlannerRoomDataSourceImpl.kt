package ajou.paran.data.local.datasourceimpl

import ajou.paran.data.db.converter.toEntity
import ajou.paran.data.db.converter.toModel
import ajou.paran.data.db.dao.PlanDao
import ajou.paran.data.local.datasource.PlannerRoomDataSource
import ajou.paran.domain.model.BasePlan
import ajou.paran.domain.model.BasePlanner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlannerRoomDataSourceImpl
@Inject
constructor(
    private val planDao: PlanDao
) : PlannerRoomDataSource {

    override suspend fun insertPlan(plan: BasePlan) = planDao.insertPlan(plan.toEntity())

    override suspend fun insertPlanner(planner: BasePlanner) = planDao.insertPlanner(planner.toEntity())

    override suspend fun deletePlanById(planId: Long) = planDao.deletePlanById(planId)

    override suspend fun deletePlannerById(plannerId: Long) = planDao.deletePlannerById(plannerId)

    override suspend fun selectPlanByIdWithDate(
        planDate: String,
        plannerId: Long
    ): List<BasePlan> = planDao.selectPlanByIdWithDate(
        planDate,
        plannerId
    ).map { it.toModel() }

    override suspend fun insertAllPlan(planList: List<BasePlan>) = planDao.insertAllPlan(
        planList.map { it.toEntity() }
    )

    override suspend fun findPlannerById(plannerId: Long): BasePlanner? = planDao.findPlannerById(plannerId)?.toModel()

    override suspend fun findFlowPlannerById(plannerId: Long): BasePlanner = planDao.findFlowPlannerById(plannerId).toModel()

    override suspend fun deleteAllPlansByPlannerId(plannerId: Long) = planDao.deleteAllPlansByPlannerId(plannerId)

    override suspend fun updatePlanner(planner: BasePlanner) = planDao.updatePlanner(planner.toEntity())

    override suspend fun updatePlan(plan: BasePlan) = planDao.updatePlan(plan.toEntity())

    override suspend fun selectAllPlanner(): List<BasePlanner> = planDao.selectAllPlanner().map { it.toModel() }

    override suspend fun deletePlanByPlannerIdWithDate(plannerId: Long, date: String) = planDao.deletePlanByPlannerIdWithDate(plannerId, date)

    override suspend fun updateIsExistCommentsWithPlanId(isExistComments: Boolean, planId: Long) = planDao.updateIsExistCommentsWithPlanId(isExistComments, planId)

}