package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class VotesUserResponse(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("photo_url")
    val photoUrl: String?,
)
