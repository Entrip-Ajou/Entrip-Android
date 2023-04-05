package ajou.paran.data.remote.model.request

import com.google.gson.annotations.SerializedName

data class UpdatePlannerRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
)
