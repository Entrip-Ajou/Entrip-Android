package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class DeleteCommentByIdResponse(
    @SerializedName("planReturnDto")
    val planResponse: PlanResponse,
    @SerializedName("commentsList")
    val commentResponse: List<CommentResponse>
)
