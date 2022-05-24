package ajou.paran.entrip.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "waiting")
data class WaitEntity(
    @PrimaryKey(autoGenerate = true) val wait_id : Int = 0,
    val user_id: String,
    val nickname: String,
    val photoUrl: String,
    val token: String,
    val planner_id : Long
) : Parcelable