package ajou.paran.entrip.repository.room.plan.dao

import ajou.paran.entrip.model.PlanEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(planEntity: PlanEntity)

    @Query("SELECT * FROM `plan` ORDER BY time ASC ")
    suspend fun selectPlan() : List<PlanEntity>?
}