package ajou.paran.entrip.repository.network.dto.community

import com.google.gson.annotations.SerializedName

data class ResponsePost(
    @SerializedName("post_id") val post_id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("author") val author: String,
    @SerializedName("likeNumber") val likeNumber: Long,
    @SerializedName("commentsNumber") val commentsNumber: Long,
    @SerializedName("photoList") val photoList: List<String>
)
