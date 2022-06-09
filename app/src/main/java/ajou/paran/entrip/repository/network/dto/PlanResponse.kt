package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName


data class PlanResponse(
    @SerializedName("plan_id") var id: Long,
    @SerializedName("date") var date: String,
    @SerializedName("todo") var todo: String,
    @SerializedName("time") var time: Int,
    @SerializedName("location") var location: String?,
    @SerializedName("planner_id") var planner_idFK: Long,
    @SerializedName("rgb") var rgb: Int,
    @SerializedName("isExistComments") var isExistComments : Boolean
)

