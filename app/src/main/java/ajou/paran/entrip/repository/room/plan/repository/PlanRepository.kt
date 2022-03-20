package ajou.paran.entrip.repository.room.plan.repository

import ajou.paran.entrip.model.PlanEntity
import kotlinx.coroutines.flow.Flow

interface PlanRepository {
    suspend fun insertPlan(planEntity: PlanEntity)
    fun selectPlan() : Flow<List<PlanEntity>>
    fun deletePlan(planEntity: PlanEntity)
    fun updatePlan(planEntity: PlanEntity)
}