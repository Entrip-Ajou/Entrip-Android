package ajou.paran.data.remote.model.request

data class NotificationRequest(
    val title: String,
    val message: String,
    val ownerId: String,
    val owner: String,
    val ownerToken: String,
    val photoUrl: String,
    val plannerId: Long,
    val plannerTitle: String,
    val isInvite: Boolean
)
