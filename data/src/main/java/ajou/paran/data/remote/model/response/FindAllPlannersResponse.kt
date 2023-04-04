package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

typealias FindAllPlannersResponseList = List<FindAllPlannersResponse>


data class FindAllPlannersResponse(
    @SerializedName("planner_id")
    val planner_id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("start_date")
    val start_date: String,
    @SerializedName("end_date")
    val end_date: String,
)