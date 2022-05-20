package ajou.paran.entrip.repository.room.plan.dao

import ajou.paran.entrip.model.WaitEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWait(waitEntity: WaitEntity)

    @Query("SELECT * FROM 'waiting' WHERE planner_id = :planner_id")
    fun selectWaiting(planner_id: Long) : Flow<List<WaitEntity>>

}