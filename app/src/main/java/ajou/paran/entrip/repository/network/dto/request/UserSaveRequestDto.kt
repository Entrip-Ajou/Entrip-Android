package ajou.paran.entrip.repository.network.dto.request

import com.google.gson.annotations.SerializedName

data class UserSaveRequestDto(
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
) {

}
