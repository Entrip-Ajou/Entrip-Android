package ajou.paran.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "plan",
    foreignKeys = [
        ForeignKey(
            entity = PlannerEntity::class,
            parentColumns = ["plannerId"],
            childColumns = ["plannerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlanEntity(
    @PrimaryKey(autoGenerate = false)
    val planId: Long,
    val plannerId: Long,
    val todo: String,
    val time: Int,
    val location: String?,
    val date: String,
    val isExistComments: Boolean
) : Parcelable
