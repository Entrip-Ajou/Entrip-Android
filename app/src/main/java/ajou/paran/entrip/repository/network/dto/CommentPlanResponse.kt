package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName

data class CommentPlanResponse(
    @SerializedName("planReturnDto") var planEntity : PlanResponse,
    @SerializedName("commentsList") var commentResponse : List<CommentResponse>
)
