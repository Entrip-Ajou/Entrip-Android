package ajou.paran.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "invite")
data class InviteEntity(
    @PrimaryKey(autoGenerate = true)
    val inviteId : Int = 0,
    val userId : String,
    val nickname: String,
    val photoUrl: String,
    val token: String,
    val plannerTitle : String,
    val plannerId : Long,
) : Parcelable