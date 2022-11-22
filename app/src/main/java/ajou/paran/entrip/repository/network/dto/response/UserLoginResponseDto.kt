package ajou.paran.entrip.repository.network.dto.response

import com.google.gson.annotations.SerializedName

data class UserLoginResponseDto(
    @SerializedName("detailedMessage")
    val detailedMessage: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("refreshToken")
    val refreshToken: String
) {
    constructor() : this(
        detailedMessage = "",
        userId = "",
        accessToken = "",
        nickname = "",
        refreshToken = ""
    )
}
