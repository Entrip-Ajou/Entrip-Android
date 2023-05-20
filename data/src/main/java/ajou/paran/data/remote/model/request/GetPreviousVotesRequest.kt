package ajou.paran.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class GetPreviousVotesRequest(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("vote_id")
    val voteId: Long
)
