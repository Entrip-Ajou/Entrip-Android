package ajou.paran.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class SaveUserAccountRequest(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("photo_url")
    val photoUrl: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("gender")
    val gender: Long,
    @SerializedName("password")
    val password: String
)
