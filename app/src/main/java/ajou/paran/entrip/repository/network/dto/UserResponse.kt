package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName

data class UserInformation(
    @SerializedName("user_id") var user_id : String,
    @SerializedName("nickname") var nickname: String,
    @SerializedName("photoUrl") var photoUrl : String,
    @SerializedName("token") var token : String
)


data class UserResponse(
    @SerializedName("user_id") var userId: String,
    @SerializedName("nickname") var nickname: String,
    @SerializedName("gender") var gender: Int,
    @SerializedName("photoUrl") var photoUrl : String,
    @SerializedName("token") var token : String
)

data class SharingFriend(
    @SerializedName("user_id") var user_id : String,
    @SerializedName("nickname") var nickname : String,
    @SerializedName("photoUrl") var photoUrl: String
)