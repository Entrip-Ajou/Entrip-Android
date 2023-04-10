package ajou.paran.data.remote.model.request

data class PostNotificationRequest(
    val data: NotificationRequest,
    val to: String
)
