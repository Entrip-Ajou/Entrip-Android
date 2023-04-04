package ajou.paran.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class SaveUserRequest(
    @SerializedName("user_id")
    val userId: String,
)
