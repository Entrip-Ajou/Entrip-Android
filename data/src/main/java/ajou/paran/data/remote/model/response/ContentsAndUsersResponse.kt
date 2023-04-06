package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class ContentsAndUsersResponse(
    @SerializedName("content_id")
    val contentId: Long,
    @SerializedName("content")
    val content: String,
    @SerializedName("users")
    val users: MutableList<VotesUserResponse>
)
