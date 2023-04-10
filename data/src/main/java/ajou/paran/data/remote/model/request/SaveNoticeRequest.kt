package ajou.paran.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class SaveNoticeRequest(
    @SerializedName("author")
    val author: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("planner_id")
    val plannerId: Long,
)
