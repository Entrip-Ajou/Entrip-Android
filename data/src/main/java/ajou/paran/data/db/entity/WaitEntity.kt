package ajou.paran.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "waiting")
data class WaitEntity(
    @PrimaryKey(autoGenerate = true)
    val waitId : Int = 0,
    val userId: String,
    val nickname: String,
    val photoUrl: String,
    val token: String,
    val plannerId : Long
) : Parcelable
