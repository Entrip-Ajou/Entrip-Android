package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class CommentResponse(
    @SerializedName("comment_id")
    val commentId: Long,
    @SerializedName("author")
    var userId: String,
    @SerializedName("content")
    var content: String,
    @SerializedName("photoUrl")
    var photoUrl: String,
    @SerializedName("nickname")
    val nickname: String
)
