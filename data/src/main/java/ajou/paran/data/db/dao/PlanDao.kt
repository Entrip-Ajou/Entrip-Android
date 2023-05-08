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
    fun selectPlanByIdWithDate(
        planDate: String,
        plannerId: Long,
    ): Flow<List<PlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPlan(
        planList: List<PlanEntity>,
    )

    @Query("SELECT * FROM 'planner' WHERE plannerId = :plannerId")
    fun findPlannerById(
        plannerId: Long,
    ): PlannerEntity?

    @Query("SELECT * FROM 'planner' WHERE plannerId = :plannerId")
    fun findFlowPlannerById(
        plannerId: Long,
    ) : Flow<PlannerEntity>

    @Query("DELETE FROM 'plan' WHERE plannerIdFK = :plannerId")
    fun deleteAllPlansByPlannerId(
        plannerId: Long,
    )

    @Update
    fun updatePlanner(
        entity: PlannerEntity,
    )

    @Update
    suspend fun updatePlan(
        entity: PlanEntity,
    )

    @Query("SELECT * FROM 'planner'")
    fun selectAllPlanner() : Flow<List<PlannerEntity>>

    @Query("DELETE FROM 'plan' WHERE plannerIdFK = :plannerId AND date= :date")
    fun deletePlanByPlannerIdWithDate(
        plannerId: Long,
        date: String,
    )

    @Query("UPDATE 'plan' SET isExistComments = :isExistComments WHERE planId = :planId")
    fun updateIsExistCommentsWithPlanId(
        isExistComments : Boolean,
        planId : Long
    )
}