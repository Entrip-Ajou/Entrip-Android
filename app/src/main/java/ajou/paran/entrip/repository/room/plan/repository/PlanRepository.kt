package ajou.paran.entrip.repository.room.plan.repository

import ajou.paran.entrip.model.PlanEntity

interface PlanRepository {
    suspend fun insertPlan(planEntity: PlanEntity)
}