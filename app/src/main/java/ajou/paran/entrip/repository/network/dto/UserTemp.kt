package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName

data class UserTemp (
    @SerializedName("user_id") var userId: String,
    @SerializedName("gender") var gender: Int,
    @SerializedName("nickname") var nickname: String
)

data class UserInformation(
    @SerializedName("nickname") var nickname: String,
    @SerializedName("photoUrl") var photoUrl : String,
    @SerializedName("token") var token : String
)


data class UserResponse(
    @SerializedName("user_id") var userId: String,
    @SerializedName("nickname") var nickname: String,
    @SerializedName("gender") var gender: Int,
    @SerializedName("travelFavorite") var travelFavorite : Int,
    @SerializedName("photoUrl") var photoUrl : String,
    @SerializedName("token") var token : String
)

data class SharingFriend(
    @SerializedName("user_id") var user_id : String,
    @SerializedName("nickname") var nickname : String,
    @SerializedName("photoUrl") var photoUrl: String
)