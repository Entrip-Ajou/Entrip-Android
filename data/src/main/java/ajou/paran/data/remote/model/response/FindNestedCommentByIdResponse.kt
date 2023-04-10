package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class FindNestedCommentByIdResponse(
    @SerializedName("postNestedComment_id")
    val nestedCommentId: Long,
    @SerializedName("content")
    val content: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("nickname")
    val nickname: String,
)
