package ajou.paran.entrip.repository.network.dto

import com.google.gson.annotations.SerializedName

data class NoticesSaveRequest(
    @SerializedName("author") var author : String,
    @SerializedName("title") var title : String,
    @SerializedName("content") var content : String,
    @SerializedName("planner_id") var planner_id : Long
)

data class NoticesUpdateRequest(
    @SerializedName("title") var title : String,
    @SerializedName("content") var content : String
)
