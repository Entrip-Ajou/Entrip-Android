package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class UpdateVotesResponse(
    @SerializedName("vote_id")
    val vote_id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("voting")
    val voting: Boolean,
    @SerializedName("host_id")
    val host_id: String,
    @SerializedName("contents")
    val contents: MutableList<VotesContentsResponse>
)
