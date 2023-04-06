package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class VotesContentsResponse(
    @SerializedName("votesContents_id")
    val votesContentsId: Long,
    @SerializedName("content")
    val content: String,
    @SerializedName("selectedCount")
    val selectedCount: Int
)
