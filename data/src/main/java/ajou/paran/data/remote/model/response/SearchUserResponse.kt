package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
    @SerializedName("user_id")
    val user_id: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("gender")
    val gender: Long,
    @SerializedName("photoUrl")
    val photoUrl: String,
    @SerializedName("token")
    val token: String
)
