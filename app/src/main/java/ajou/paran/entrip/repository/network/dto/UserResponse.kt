package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName

data class UserResponse (
    @SerializedName("user_id") var userId: String,
    @SerializedName("gender") var gender: Int,
    @SerializedName("nickname") var nickname: String
)