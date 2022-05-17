package ajou.paran.entrip.repository.network.dto

data class NotificationData(
    val title : String,
    val message : String,
    val owner : String,
    val owner_token : String,
    val planner_id : Long,
    val isInvite : Boolean
)