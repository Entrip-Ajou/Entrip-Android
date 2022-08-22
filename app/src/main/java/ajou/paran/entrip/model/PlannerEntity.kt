package ajou.paran.entrip.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "planner")
data class PlannerEntity(
    @PrimaryKey(autoGenerate = false) val planner_id: Long,
    val title : String,
    val start_date : String,
    val end_date : String,
) : Parcelable
