package ajou.paran.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BasePlanner(
    val id: Long,
    val title: String,
    val startDate: String,
    val endDate: String,
    val timeStamp: String,
    val commentTimeStamp: String,
) : Parcelable