package ajou.paran.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class UpdateNoticeByIdRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
)
