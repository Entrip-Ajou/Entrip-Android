package ajou.paran.entrip.repository.network.dto.response

import com.google.gson.annotations.SerializedName

data class UserReissueAccessTokenResponseDto(
    @SerializedName("data")
    val accessToken: String
) {

}
