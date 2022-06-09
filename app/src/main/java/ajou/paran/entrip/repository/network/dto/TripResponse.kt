package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName

data class TripResponse (
    @SerializedName("name") var name: String,
    @SerializedName("address") var address: String,
    @SerializedName("photoUrl") var photoUrl : String,
    @SerializedName("tags") var tags: List<String>
)