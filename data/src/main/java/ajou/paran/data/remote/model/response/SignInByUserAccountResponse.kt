package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class SignInByUserAccountResponse(
    @SerializedName("detailedMessage")
    val detailedMessage: String = "",
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
)
