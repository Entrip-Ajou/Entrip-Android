package ajou.paran.entrip.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plan")
data class PlanEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L,
    @ColumnInfo(name="todo")
    val todo : String,
    @ColumnInfo(name="rgb")
    val rgb : Int,
    @ColumnInfo(name="time")
    val time : Int,
    @ColumnInfo(name="location")
    val location : String?,

    // 희훈님 파트 column 추가
    @ColumnInfo(name="date")
    val date : String,
//    @ColumnInfo(name="title")
//    val title : String,
    @ColumnInfo(name="planner_id")
    val planner_id : String
)
