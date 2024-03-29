package ajou.paran.domain.repository

import ajou.paran.domain.model.BasePlan

interface PlanRepository {

    suspend fun addPlan(
        plannerId: Long,
        date: String,
        todo: String,
        time: Int,
        location: String?
    )

    suspend fun insertPlan(
        plan: BasePlan
    )

    suspend fun deletePlanById(
        planId: Long
    )

    suspend fun selectPlanByIdWithDate(
        planDate: String,
        plannerId: Long
    ): List<BasePlan>

    suspend fun insertAllPlan(
        planList: List<BasePlan>
    )

    suspend fun updatePlan(
        plan: BasePlan
    )


}