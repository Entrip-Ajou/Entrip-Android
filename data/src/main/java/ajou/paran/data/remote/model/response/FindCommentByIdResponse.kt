package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class FindCommentByIdResponse(
    @SerializedName("postComment_id")
    val commentId: Long,
    @SerializedName("content")
    val content: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("nickname")
    val nickname: String,
)
