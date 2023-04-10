package ajou.paran.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("httpStatus")
    val statusCode: Int,
    @SerializedName("message")
    val statusMessage: String,
    @SerializedName("data")
    val data: T,
)