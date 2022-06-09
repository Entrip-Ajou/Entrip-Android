package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName

data class CommentResponse(
    @SerializedName("comment_id") var comment_id : Long,
    @SerializedName("author") var user_id : String,
    @SerializedName("content") var content : String,
    @SerializedName("photoUrl") var photoUrl : String,
    @SerializedName("nickname") var nickname : String
)