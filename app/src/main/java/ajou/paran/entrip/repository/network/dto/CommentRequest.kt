package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName

data class CommentRequest(
    @SerializedName("author") var user_id: String,
    @SerializedName("content") var content: String,
    @SerializedName("plans_id") var plan_id: Long
)