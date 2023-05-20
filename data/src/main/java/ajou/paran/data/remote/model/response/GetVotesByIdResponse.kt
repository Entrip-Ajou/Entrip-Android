package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class GetVotesByIdResponse(
    @SerializedName("title")
    val title: String,
    @SerializedName("contentsAndUsers")
    val contentsAndUsers: MutableList<ContentsAndUsersResponse>,
    @SerializedName("multipleVotes")
    val multipleVotes: Boolean,
    @SerializedName("anonymousVote")
    val anonymousVote: Boolean,
    @SerializedName("host_id")
    val hostId: String,
    @SerializedName("voting")
    val voting: Boolean
)
