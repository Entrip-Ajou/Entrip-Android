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

    @Update
    fun updatePlan(planEntity : PlanEntity)

    @Query("SELECT * FROM `plan` ORDER BY time ASC ")
    fun selectPlan() : Flow<List<PlanEntity>>
}