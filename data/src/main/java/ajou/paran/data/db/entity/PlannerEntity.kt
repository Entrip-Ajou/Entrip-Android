package ajou.paran.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "planner")
data class PlannerEntity(
    @PrimaryKey(autoGenerate = false)
    val plannerId: Long,
    val title : String,
    val startDate : String,
    val endDate : String,
) : Parcelable
