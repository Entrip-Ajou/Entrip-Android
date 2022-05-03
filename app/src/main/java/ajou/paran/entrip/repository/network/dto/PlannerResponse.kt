package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName

data class PlannerResponse(
    @SerializedName("planner_id") var planner_id: Long,
    @SerializedName("title") var title : String,
    @SerializedName("start_date") var start_date: String,
    @SerializedName("end_date") var end_date: String,
    @SerializedName("time_stamp") var timeStamp : String,
    @SerializedName("user_timestamp") var user_timeStamp : String,
    @SerializedName("comment_timestamp") var comment_timeStamp : String
)