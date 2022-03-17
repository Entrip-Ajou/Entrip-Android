package ajou.paran.entrip.repository.room.plan.repository

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.repository.room.AppDatabase
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(private val db:AppDatabase) : PlanRepository {
    override suspend fun insertPlan(planEntity: PlanEntity) {
        db.planDao().insertPlan(planEntity)
    }

    override suspend fun selectPlan(): List<PlanEntity>? {
        return db.planDao().selectPlan()
    }

}