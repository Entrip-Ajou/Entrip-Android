package ajou.paran.data.remote.datasource

import ajou.paran.domain.model.BasePlan

interface PlanRemoteDataSource {

    suspend fun addPlan(
        plannerId: Long,
        date: String,
        todo: String,
        time: Int,
        location: String? = null,
    ): BasePlan

    suspend fun deletePlanById(
        planId: Long
    )

    suspend fun updatePlanById(
        planId: Long,
        date: String,
        todo: String,
        time: Int,
        location: String? = null,
    )

}