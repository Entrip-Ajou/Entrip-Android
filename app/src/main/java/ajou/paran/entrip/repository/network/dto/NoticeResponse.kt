package ajou.paran.entrip.repository.network.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Parcelize
data class NoticeResponse(
    @SerializedName("notice_id") var notice_id : Long,
    @SerializedName("author") var author : String,
    @SerializedName("nickname") var nickname : String,
    @SerializedName("title") var title : String,
    @SerializedName("content") var content : String,
    @SerializedName("modifiedDate") var modifiedDate : String
) : Parcelable
