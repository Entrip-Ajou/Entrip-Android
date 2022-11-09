package ajou.paran.entrip.repository.network.dto.community

import com.google.gson.annotations.SerializedName

data class ResponseNestedComment(
    @SerializedName("postNestedComment_id")
    val nestedCommentId: Long,
    @SerializedName("content")
    val content: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("nickname")
    val nickname: String
) {
    constructor(nestedCommentId: Long) : this(
        nestedCommentId = nestedCommentId,
        content = "",
        author = "",
        nickname = ""
    )
}
