package ajou.paran.entrip.repository.room.plan.dao

import ajou.paran.entrip.model.PlanEntity
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(planEntity: PlanEntity)

    @Delete
    fun deletePlan(planEntity: PlanEntity)

    @Query("Update 'plan' SET todo = :todo, rgb = :rgb, time = :time, location = :location WHERE id = :id")
    fun updatePlan(todo:String, rgb:Int, time : Int, location : String, id : Long)

    @Query("SELECT * FROM `plan` WHERE date = :planDate AND planner_id = :plannerId ORDER BY time ASC")
    fun selectPlan(planDate : String, plannerId : String) : Flow<List<PlanEntity>>
}