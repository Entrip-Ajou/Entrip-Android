package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

typealias GetAllNestedCommentsByPostCommentIdResponseList = List<GetAllNestedCommentsByPostCommentIdResponse>

data class GetAllNestedCommentsByPostCommentIdResponse(
    @SerializedName("postNestedComment_id")
    val nestedCommentId: Long,
    @SerializedName("content")
    val content: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("nickname")
    val nickname: String,
)
