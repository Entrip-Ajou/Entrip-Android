package ajou.paran.entrip.repository.room.plan.repository

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.repository.room.AppDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(private val db:AppDatabase) : PlanRepository {
    override suspend fun insertPlan(planEntity: PlanEntity) {
        db.planDao().insertPlan(planEntity)
    }

    override fun selectPlan(): Flow<List<PlanEntity>> {
        return db.planDao().selectPlan()
    }

}