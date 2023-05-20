package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class FindPhotoByIdResponse(
    @SerializedName("photo_id")
    val photoId: Long,
    @SerializedName("photoUrl")
    val photoUrl: String,
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("priority")
    val priority: Long,
)
