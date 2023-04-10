package ajou.paran.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class SavePostRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("photoIdList")
    val photoIdList: List<Long> = listOf()
)
