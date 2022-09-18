package ajou.paran.entrip.repository.network.dto.community

import com.google.gson.annotations.SerializedName

data class ResponseFindByIdPhoto(
    @SerializedName("photo_id")
    val photoId: Long,
    @SerializedName("photoUrl")
    val photoUrl: String,
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("priority")
    val priority: Long
)