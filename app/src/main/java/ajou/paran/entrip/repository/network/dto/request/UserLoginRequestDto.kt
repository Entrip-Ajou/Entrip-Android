package ajou.paran.entrip.repository.network.dto.request

import com.google.gson.annotations.SerializedName

data class UserLoginRequestDto(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("password")
    val password: String
) {

}
