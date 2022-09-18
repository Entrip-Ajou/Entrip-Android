package ajou.paran.entrip.repository.network.dto.community

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part

data class RequestSavePhoto(
    @SerializedName("photos")
    val photos: MultipartBody.Part
)