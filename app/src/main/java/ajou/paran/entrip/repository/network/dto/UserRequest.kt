package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("user_id") var userId: String,
)