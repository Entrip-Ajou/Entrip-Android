package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class FindPlannerByIdResponse(
    @SerializedName("planner_id")
    val plannerId: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("time_stamp")
    val timeStamp: String,
    @SerializedName("comment_timeStamp")
    val commentTimeStamp: String
)
