package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("user_id") var userId: String,
)

data class UserTemp(
    @SerializedName("user_id") var userId: String,
    @SerializedName("gender") var gender: Int,
    @SerializedName("nickname") var nickname:String,
    @SerializedName("photoUrl") var photoUrl : String
)