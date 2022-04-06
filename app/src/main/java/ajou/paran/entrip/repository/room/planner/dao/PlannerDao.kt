package ajou.paran.entrip.repository.room.planner.dao

import ajou.paran.entrip.model.test.PlannerTestEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlannerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanner(plannerTestEntity: PlannerTestEntity)

    @Query("Update 'planner' SET title = :title, start_date = :start_date, end_date = :end_date, time_stamp = :time_stamp WHERE id = :id")
    fun updatePlanner(title : String, start_date : String, end_date : String, time_stamp : String, id : Long)

    @Query("SELECT * FROM 'planner' WHERE start_date = :start_date AND end_date = :end_date AND id = :id")
    fun selectPlanner(start_date : String, end_date : String, id : Long) : Flow<List<PlannerTestEntity>>

//    @Query("SELECT * FROM 'planner' WHERE title = :title")
//    fun titlePlanner(title : String, id : Long) : Flow<String>
}