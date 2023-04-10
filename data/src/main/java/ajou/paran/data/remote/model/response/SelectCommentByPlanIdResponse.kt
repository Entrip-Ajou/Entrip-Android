package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

typealias SelectCommentByPlanIdResponseList = List<SelectCommentByPlanIdResponse>

data class SelectCommentByPlanIdResponse(
    @SerializedName("comment_id")
    val commentId: Long,
    @SerializedName("author")
    val userId: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("photoUrl")
    val photoUrl: String,
    @SerializedName("nickname")
    val nickname: String
)
