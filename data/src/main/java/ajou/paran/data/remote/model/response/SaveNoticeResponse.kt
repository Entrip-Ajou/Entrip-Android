package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class SaveNoticeResponse(
    @SerializedName("notice_id")
    val noticeId: Long,
    @SerializedName("author")
    val author: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("modifiedDate")
    val modifiedDate: String,
)
