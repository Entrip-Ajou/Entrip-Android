package ajou.paran.data.remote.datasource

interface PlanRemoteDataSource {

    suspend fun addPlan(
        plannerId: Long,
        date: String,
        todo: String,
        time: Int,
        location: String? = null,
    )

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