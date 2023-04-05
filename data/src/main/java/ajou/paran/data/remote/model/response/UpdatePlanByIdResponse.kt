package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class UpdatePlanByIdResponse(
    @SerializedName("plan_id")
    val planId: Long,
    @SerializedName("date")
    val date: String,
    @SerializedName("todo")
    val todo: String,
    @SerializedName("time")
    val time: Int,
    @SerializedName("location")
    val location: String?,
    @SerializedName("planner_id")
    val plannerId: Long,
    @SerializedName("isExistComments")
    val isExistComments: Boolean
)
