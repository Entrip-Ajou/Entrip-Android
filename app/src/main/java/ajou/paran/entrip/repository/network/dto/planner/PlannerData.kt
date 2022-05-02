package ajou.paran.entrip.repository.network.dto.planner

import com.google.gson.annotations.SerializedName

data class PlannerData(
    @SerializedName("planner_id") var planner_id: Long,
    @SerializedName("title") var title : String,
    @SerializedName("start_date") var start_date: String,
    @SerializedName("end_date") var end_date: String,
    @SerializedName("time_stamp") var timeStamp : String
)
