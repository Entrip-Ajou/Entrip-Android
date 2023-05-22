package ajou.paran.domain.model

data class BasePlan(
    val planId: Long,
    val plannerId: Long,
    val todo: String,
    val time: Int,
    val location: String?,
    val date: String,
    val rgb: Long,
    val isExistComments: Boolean
)
