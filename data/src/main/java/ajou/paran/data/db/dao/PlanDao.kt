package ajou.paran.data.db.dao

import ajou.paran.data.db.entity.PlanEntity
import ajou.paran.data.db.entity.PlannerEntity
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(
        entity: PlanEntity,
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanner(
        entity: PlannerEntity,
    )

    @Query("DELETE FROM 'plan' WHERE planId = :planId")
    suspend fun deletePlanById(
        planId: Long,
    )

    @Query("DELETE FROM 'planner' WHERE plannerId = :plannerId")
    suspend fun deletePlannerById(
        plannerId: Long,
    )

    @Query("SELECT * FROM `plan` WHERE date = :planDate AND plannerIdFK = :plannerId ORDER BY time ASC")
    suspend fun selectPlanByIdWithDate(
        planDate: String,
        plannerId: Long,
    ): List<PlanEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPlan(
        planList: List<PlanEntity>,
    )

    @Query("SELECT * FROM 'planner' WHERE plannerId = :plannerId")
    suspend fun findPlannerById(
        plannerId: Long,
    ): PlannerEntity?

    @Query("SELECT * FROM 'planner' WHERE plannerId = :plannerId")
    suspend fun findFlowPlannerById(
        plannerId: Long,
    ): PlannerEntity

    @Query("DELETE FROM 'plan' WHERE plannerIdFK = :plannerId")
    suspend fun deleteAllPlansByPlannerId(
        plannerId: Long,
    )

    @Update
    suspend fun updatePlanner(
        entity: PlannerEntity,
    )

    @Update
    suspend fun updatePlan(
        entity: PlanEntity,
    )

    @Query("SELECT * FROM 'planner'")
    suspend fun selectAllPlanner(): List<PlannerEntity>

    @Query("DELETE FROM 'plan' WHERE plannerIdFK = :plannerId AND date= :date")
    suspend fun deletePlanByPlannerIdWithDate(
        plannerId: Long,
        date: String,
    )

    @Query("UPDATE 'plan' SET isExistComments = :isExistComments WHERE planId = :planId")
    suspend fun updateIsExistCommentsWithPlanId(
        isExistComments : Boolean,
        planId : Long
    )
}