package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName

data class PlanRequest(
    @SerializedName("date") var date: String,
    @SerializedName("todo") var todo: String,
    @SerializedName("time") var time: Int,
    @SerializedName("location") var location: String?,
    @SerializedName("rgb") var RGB: Int,
    @SerializedName("planner_id") var planner_idFK: Long
)

data class PlanUpdateRequest(
    @SerializedName("date") var date:String,
    @SerializedName("todo") var todo: String,
    @SerializedName("time") var time: Int,
    @SerializedName("location") var location: String?,
    @SerializedName("rgb") var RGB: Int
)