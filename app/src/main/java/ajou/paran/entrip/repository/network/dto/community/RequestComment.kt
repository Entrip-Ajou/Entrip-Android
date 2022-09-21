package ajou.paran.entrip.repository.network.dto.community

import com.google.gson.annotations.SerializedName

data class RequestComment(
    @SerializedName("author")
    val author: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("post_id")
    val postId: Long
)