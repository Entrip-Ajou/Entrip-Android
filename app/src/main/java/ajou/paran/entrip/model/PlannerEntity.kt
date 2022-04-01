package ajou.paran.entrip.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planner")
data class PlannerEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L,
    @ColumnInfo(name="title")
    val title : String,
    @ColumnInfo(name="start_date")
    val start_date : String,
    @ColumnInfo(name="end_date")
    val end_date : String,
    @ColumnInfo(name="time_stamp")
    val time_stamp : String,
)