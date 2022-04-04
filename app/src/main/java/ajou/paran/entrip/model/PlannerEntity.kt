package ajou.paran.entrip.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "planner")
data class PlannerEntity(
    @PrimaryKey(autoGenerate = false) val planner_id: Long,
    val title : String,
    val start_date : String,
    val end_date : String,
    val timeStamp: LocalDateTime
)
