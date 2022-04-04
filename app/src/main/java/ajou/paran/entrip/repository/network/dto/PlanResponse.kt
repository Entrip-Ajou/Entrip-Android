package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName


data class PlanResponse(
    @SerializedName("id") var id: Long,
    @SerializedName("planner_idFK") var planner_idFK: Long,
    @SerializedName("todo") var todo: String,
    @SerializedName("rgb") var rgb: Int,
    @SerializedName("time") var time: Int,
    @SerializedName("location") var location: String?,
    @SerializedName("date") var date: String
)

