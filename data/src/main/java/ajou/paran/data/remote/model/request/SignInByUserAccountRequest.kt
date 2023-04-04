package ajou.paran.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class SignInByUserAccountRequest(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("password")
    val password: String
)
