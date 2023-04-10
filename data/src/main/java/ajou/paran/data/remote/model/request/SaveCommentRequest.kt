package ajou.paran.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class SaveCommentRequest(
    @SerializedName("author")
    val author: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("post_id")
    val postId: Long,
)
