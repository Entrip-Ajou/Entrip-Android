package ajou.paran.data.remote.datasource

interface PlannerRemoteDataSource {

    suspend fun createPlannerByUserId(
        userId: String
    )

    suspend fun deletePlanner(
        userId: String,
        plannerId: Long,
    )

    suspend fun fetchPlannerByPlannerId(
        plannerId: Long,
    )

    suspend fun fetchPlansInPlannerByPlannerId(
       plannerId: Long
    )

    suspend fun updatePlanner(
        plannerId: Long,
        title: String,
        startDate: String,
        endDate: String,
    )

    suspend fun findPlanByPlannerIdWithDate(
        plannerId: Long,
        date: String
    )

    suspend fun isExistPlannerById(
        plannerId: Long
    )

}