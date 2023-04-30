package ajou.paran.domain.model

data class DefaultUser(
    val userId: String,
    val nickname: String,
    val bearerToken: Token
)
