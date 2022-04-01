package ajou.paran.entrip.repository.room.planner.repository

import ajou.paran.entrip.model.PlannerEntity
import kotlinx.coroutines.flow.Flow

interface PlannerRepository {
    suspend fun insertPlanner(plannerEntity: PlannerEntity)
    fun updatePlanner(title : String, start_date : String, end_date : String, time_stamp : String, id : Long)
    fun selectPlanner(start_date : String, end_date : String, id : Long) : Flow<List<PlannerEntity>>
}