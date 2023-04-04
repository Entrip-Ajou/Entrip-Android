package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class SaveUserResponse(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("photoUrl")
    val photoUrl: String
)
