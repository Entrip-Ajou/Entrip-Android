package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

typealias FindAllUsersByPlannerIdResponseList = List<FindAllUsersByPlannerIdResponse>

data class FindAllUsersByPlannerIdResponse(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("photoUrl")
    val photoUrl: String,
    @SerializedName("token")
    val token: String
)
