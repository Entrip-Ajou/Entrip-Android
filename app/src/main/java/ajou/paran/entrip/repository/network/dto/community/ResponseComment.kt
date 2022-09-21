package ajou.paran.entrip.repository.network.dto.community

import com.google.gson.annotations.SerializedName

data class ResponseComment(
    @SerializedName("postComment_id")
    val commentId: Long,
    @SerializedName("content")
    val content: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("nickname")
    val nickname: String
) {
    constructor(
        commentId: Long
    ) : this(
        commentId = 1L,
        content = "",
        author = "",
        nickname = ""
    )
}