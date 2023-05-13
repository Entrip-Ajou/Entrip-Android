package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

typealias FindAllVotesByPlannerIdResponseList = List<FindAllVotesByPlannerIdResponse>

data class FindAllVotesByPlannerIdResponse(
    @SerializedName("vote_id")
    val voteId: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("voting")
    val voting: Boolean,
    @SerializedName("host_id")
    val hostId : String,
    @SerializedName("contents")
    val contents: MutableList<VotesContentsResponse>
)
