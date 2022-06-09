package ajou.paran.entrip.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "plan",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = PlannerEntity::class,
            parentColumns = ["planner_id"],
            childColumns = ["planner_idFK"],
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class PlanEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val planner_idFK : Long,
    val todo : String,
    val rgb : Int,
    val time : Int,
    val location : String?,
    val date : String,
    val isExistComments : Boolean
) : Parcelable


