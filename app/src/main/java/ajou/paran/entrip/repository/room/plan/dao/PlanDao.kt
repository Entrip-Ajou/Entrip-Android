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

    @Query("DELETE FROM 'plan' WHERE id = :plan_id")
    suspend fun deletePlan(plan_id : Long)

    @Query("DELETE FROM 'planner' WHERE planner_id = :planner_id")
    suspend fun deletePlanner(planner_id : Long)

    @Query("SELECT * FROM `plan` WHERE date = :planDate AND planner_idFK = :plannerId ORDER BY time ASC")
    fun selectPlan(planDate : String, plannerId : Long) : Flow<List<PlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPlan(plans : List<PlanEntity>)

    @Query("SELECT * FROM 'planner' WHERE planner_id = :planner_Id")
    fun findPlanner(planner_Id : Long) : PlannerEntity?

    @Query("SELECT * FROM 'planner' WHERE planner_id = :planner_Id")
    fun findFlowPlanner(planner_Id : Long) : Flow<PlannerEntity>

    @Query("DELETE FROM 'plan' WHERE planner_idFK = :planner_Id")
    fun deleteAllPlan(planner_Id: Long)

    @Update
    fun updatePlanner(plannerEntity: PlannerEntity)

    @Update
    suspend fun updatePlan(planEntity: PlanEntity)

    @Query("SELECT * FROM 'planner'")
    fun selectAllPlanner() : Flow<List<PlannerEntity>>
}