package ajou.paran.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class SaveNestedCommentRequest(
    @SerializedName("author")
    val author: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("postComment_id")
    val commentId: Long,
)
