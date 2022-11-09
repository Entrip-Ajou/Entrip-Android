package ajou.paran.entrip.repository.network.dto.community

import com.google.gson.annotations.SerializedName

data class RequestPost(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("author") val author: String,
    @SerializedName("photoIdList") val photoIdList: List<Long> = listOf()
)