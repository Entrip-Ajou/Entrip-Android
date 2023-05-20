package ajou.paran.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class UpdatePlanByIdRequest(
    @SerializedName("date")
    val date: String,
    @SerializedName("todo")
    val todo: String,
    @SerializedName("time")
    val time: Int,
    @SerializedName("location")
    val location: String?,
)
