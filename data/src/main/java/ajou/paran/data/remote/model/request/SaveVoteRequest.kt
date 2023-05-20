package ajou.paran.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class SaveVoteRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("contents")
    val contents: MutableList<String>,
    @SerializedName("multipleVotes")
    val multipleVotes: Boolean,
    @SerializedName("anonymousVotes")
    val anonymousVotes: Boolean,
    @SerializedName("deadLine")
    val deadLine: String?,
    @SerializedName("planner_id")
    val plannerId: Long,
    @SerializedName("author")
    val author: String,
)
