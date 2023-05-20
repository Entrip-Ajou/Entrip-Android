package ajou.paran.data.db

import ajou.paran.data.db.dao.PlanDao
import ajou.paran.data.db.dao.UserDao
import ajou.paran.data.db.entity.InviteEntity
import ajou.paran.data.db.entity.PlanEntity
import ajou.paran.data.db.entity.PlannerEntity
import ajou.paran.data.db.entity.WaitEntity
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        PlanEntity::class,
        PlannerEntity::class,
        WaitEntity::class,
        InviteEntity::class
    ],
    version = 1
)

abstract class EntripDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "EntripDatabase.db"
    }

    abstract fun planDao() : PlanDao
    abstract fun userDao() : UserDao
}