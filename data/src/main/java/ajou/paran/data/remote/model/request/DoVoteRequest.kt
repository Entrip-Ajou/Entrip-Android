package ajou.paran.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class DoVoteRequest(
    @SerializedName("vote_id")
    val votesId: Long,
    @SerializedName("voteContents_id")
    val voteContentIds: MutableList<Long>,
    @SerializedName("user_id")
    val userId: String
)
