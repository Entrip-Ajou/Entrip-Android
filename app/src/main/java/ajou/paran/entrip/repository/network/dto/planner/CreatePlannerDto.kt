package ajou.paran.entrip.repository.network.dto.planner

import com.google.gson.annotations.SerializedName

data class CreatePlannerDto(
    @SerializedName("user_id") var userId: String
)