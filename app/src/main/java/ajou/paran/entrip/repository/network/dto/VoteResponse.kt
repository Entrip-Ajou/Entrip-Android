package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName

data class VoteResponse(
    @SerializedName("vote_id") val vote_id : Long,
    @SerializedName("title") val title : String,
    @SerializedName("voting") val voting : Boolean,
    @SerializedName("host_id") val host_id : String,
    @SerializedName("contents") val contents : MutableList<VotesContentsReturnDto>
)

data class VotesContentsReturnDto(
    @SerializedName("votesContents_id") val votesContenst_id : Long,
    @SerializedName("content") val content : String,
    @SerializedName("selectedCount") val selectedCount : Int
)

data class VotesFullInfoReturnDto(
    @SerializedName("title") val title : String,
    @SerializedName("contentsAndUsers") val contentsAndUsers : MutableList<UsersAndContentsReturnDto>,
    @SerializedName("multipleVotes") val multipleVotes: Boolean,
    @SerializedName("anonymousVote") val anonymousVote: Boolean,
    @SerializedName("host_id") val host_id : String,
    @SerializedName("voting") val voting : Boolean
)

data class UsersAndContentsReturnDto(
    @SerializedName("content_id") val contentId : Long,
    @SerializedName("content") val content : String,
    @SerializedName("users") val users : MutableList<VotesUserReturnDto>
)

data class VotesUserReturnDto(
    @SerializedName("user_id") val userId : String,
    @SerializedName("nickname") val nickname : String,
    @SerializedName("photo_url") val photo_url : String?
)