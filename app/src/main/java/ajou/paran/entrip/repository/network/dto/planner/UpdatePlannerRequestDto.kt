package ajou.paran.entrip.repository.network.dto.planner

import com.google.gson.annotations.SerializedName

data class UpdatePlannerRequestDto(
    @SerializedName("title") var title: String,
    @SerializedName("start_date") var start_date: String,
    @SerializedName("end_date") var end_date: String,
)