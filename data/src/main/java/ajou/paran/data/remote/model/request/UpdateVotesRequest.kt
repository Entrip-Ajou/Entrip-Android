package ajou.paran.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class UpdateVotesRequest(
    @SerializedName("vote_id")
    val voteId: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("multipleVote")
    val multipleVote: Boolean,
    @SerializedName("anonymousVote")
    val anonymousVote: Boolean,
    @SerializedName("deadLine")
    val deadLine: String
)
