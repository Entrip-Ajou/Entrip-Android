package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName

data class PlannerRequest(
    @SerializedName("title") var title: String,
    @SerializedName("start_date") var start_date: String,
    @SerializedName("end_date") var end_date: String,
    @SerializedName("user_id") var user_id : String
)

data class PlannerUpdateRequest(
    @SerializedName("title") var title: String,
    @SerializedName("start_date") var start_date: String,
    @SerializedName("end_date") var end_date: String,
)