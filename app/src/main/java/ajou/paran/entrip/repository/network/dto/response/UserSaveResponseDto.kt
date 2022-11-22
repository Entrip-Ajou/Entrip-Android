package ajou.paran.entrip.repository.network.dto.response

import com.google.gson.annotations.SerializedName

data class UserSaveResponseDto(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("gender")
    val gender: Long,
    @SerializedName("photoUrl")
    val photoUrl: String,
    @SerializedName("token")
    val token: String
) {
    constructor(): this(
        userId = "",
        nickname = "",
        gender = -1L,
        photoUrl = "",
        token = ""
    )
}
