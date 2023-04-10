package ajou.paran.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class InsertCommentRequest(
    @SerializedName("author")
    val userId: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("plans_id")
    val planId: Long
)
