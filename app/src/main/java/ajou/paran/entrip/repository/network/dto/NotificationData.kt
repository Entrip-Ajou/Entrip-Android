package ajou.paran.entrip.repository.network.dto

data class NotificationData(
    val title : String,
    val message : String,
    val owner_id : String,
    val owner : String,
    val owner_token : String,
    val photo_url : String,
    val planner_id : Long,
    val planner_title : String,
    val isInvite : Boolean
)