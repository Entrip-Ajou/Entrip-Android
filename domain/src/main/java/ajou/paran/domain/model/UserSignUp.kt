package ajou.paran.domain.model

data class UserSignUp(
    val userId: String,
    val nickname: String,
    val gender: Long,
    val photoUrl: String,
)