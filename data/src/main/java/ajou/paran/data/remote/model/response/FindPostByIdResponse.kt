package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class FindPostByIdResponse(
    @SerializedName("post_id")
    val postId: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("likeNumber")
    val likeNumber: Long,
    @SerializedName("commentsNumber")
    val commentsNumber: Long,
    @SerializedName("photoList")
    val photoList: List<String>
)
