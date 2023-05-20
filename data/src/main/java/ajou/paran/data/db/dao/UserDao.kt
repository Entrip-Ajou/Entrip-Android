package ajou.paran.data.db.dao

import ajou.paran.data.db.entity.InviteEntity
import ajou.paran.data.db.entity.WaitEntity
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWait(
        entity: WaitEntity
    )

    @Query("SELECT * FROM 'waiting' WHERE plannerId = :plannerId")
    fun selectWaiting(
        plannerId: Long,
    ): Flow<List<WaitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvite(
        entity: InviteEntity
    )

    @Query("SELECT * FROM 'invite'")
    fun selectInvite(): Flow<List<InviteEntity>>

    @Query("SELECT COUNT(*) FROM 'invite'")
    fun countInvite(): Flow<Int>

    @Query("SELECT EXISTS(SELECT * FROM 'waiting' WHERE userId = :userId AND plannerId = :plannerId)")
    fun isExistNickname(
        userId: String,
        plannerId : Long,
    ): Boolean

    @Delete
    fun deleteInvite(
        entity: InviteEntity,
    )

    @Query("DELETE FROM 'waiting' WHERE userId = :userId AND plannerId = :plannerId")
    fun deleteWaiting(
        userId: String,
        plannerId: Long,
    )

    @Query("DELETE FROM 'waiting' WHERE plannerId = :plannerId")
    fun deleteWaitWithPlannerId(
        plannerId: Long
    )

}