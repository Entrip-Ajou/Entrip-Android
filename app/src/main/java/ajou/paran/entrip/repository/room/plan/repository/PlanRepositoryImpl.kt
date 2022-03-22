package ajou.paran.entrip.repository.room.plan.repository

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.repository.room.AppDatabase
import ajou.paran.entrip.repository.room.plan.dao.PlanDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(private val planDao: PlanDao) : PlanRepository {
    override suspend fun insertPlan(planEntity: PlanEntity) {
        planDao.insertPlan(planEntity)
    }

    override fun selectPlan(planDate : String, plannerId : String): Flow<List<PlanEntity>> {
        return planDao.selectPlan(planDate, plannerId)
    }

    override fun deletePlan(planEntity: PlanEntity) {
        planDao.deletePlan(planEntity)
    }

    override fun updatePlan(todo:String, rgb:Int, time : Int, location : String, id : Long) {
        planDao.updatePlan(todo, rgb, time, location, id)
    }

}