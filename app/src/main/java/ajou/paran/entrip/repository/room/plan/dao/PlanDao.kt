package ajou.paran.entrip.repository.room.plan.dao

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(planEntity: PlanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanner(plannerEntity: PlannerEntity)

    @Delete
    fun deletePlan(planEntity: PlanEntity)

    @Query("Update 'plan' SET todo = :todo, rgb = :rgb, time = :time, location = :location WHERE id = :id")
    fun updatePlan(todo:String, rgb:Int, time : Int, location : String, id : Long)

    @Query("SELECT * FROM `plan` WHERE date = :planDate AND planner_idFK = :plannerId ORDER BY time ASC")
    fun selectPlan(planDate : String, plannerId : Long) : List<PlanEntity>

    @Query("SELECT * FROM 'plan' WHERE planner_idFK = :plannerId")
    suspend fun selectAllPlan(plannerId: Long) : List<PlanEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPlan(plans : List<PlanEntity>)

    @Query("SELECT * FROM 'planner' WHERE planner_id = :planner_Id")
    suspend fun findPlanner(planner_Id : Long) : PlannerEntity

    @Query("DELETE FROM 'plan' WHERE planner_idFK = :planner_Id")
    fun deleteAllPlan(planner_Id: Long)

    @Update
    fun updatePlanner(plannerEntity: PlannerEntity)
}