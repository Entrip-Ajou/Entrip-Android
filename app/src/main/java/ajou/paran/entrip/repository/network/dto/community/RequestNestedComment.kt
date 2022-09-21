package ajou.paran.entrip.repository.network.dto.community

import com.google.gson.annotations.SerializedName

data class RequestNestedComment(
    @SerializedName("author")
    val author: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("postComment_id")
    val commentId: Long
)
