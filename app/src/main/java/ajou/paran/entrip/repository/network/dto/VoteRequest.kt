package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName

data class VotesSaveRequestDto(
    @SerializedName("title") val title : String,
    @SerializedName("contents") val contents : MutableList<String>,
    @SerializedName("multipleVotes") val multipleVotes : Boolean,
    @SerializedName("anonymousVotes") val anonymousVotes : Boolean,
    @SerializedName("deadLine") val deadLine : String?,
    @SerializedName("planner_id") val planner_id : Long,
    @SerializedName("author") val author : String,
)

data class VotesUpdateRequestDto(
    @SerializedName("vote_id") val vote_id : Long,
    @SerializedName("title") val title : String,
    @SerializedName("multipleVote") val multipleVote : Boolean,
    @SerializedName("anonymousVote") val anonymousVote : Boolean,
    @SerializedName("deadLine") val deadLine : String
)

data class VotesContentsCountRequestDto(
    @SerializedName("vote_id") val votesId : Long,
    @SerializedName("voteContents_id") val voteContentIds : MutableList<Long>,
    @SerializedName("user_id") val userId : String
)

data class PreviousVotesContentsRequestDto(
    @SerializedName("user_id") val user_id : String,
    @SerializedName("vote_id") val vote_id : Long
)