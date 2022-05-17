package ajou.paran.entrip.repository.room.plan.dao

import ajou.paran.entrip.model.WaitEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWait(waitEntity: WaitEntity)
}