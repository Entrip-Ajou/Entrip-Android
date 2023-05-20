package ajou.paran.domain.model

sealed class DefaultUser(
    open val userId: String,
    open val nickname: String,
    open val bearerToken: Token
) {

    data class User(
        override val userId: String,
        override val nickname: String,
        override val bearerToken: Token
    ) : DefaultUser(
        userId = userId,
        nickname = nickname,
        bearerToken = bearerToken,
    )

    data class PlannerUser(
        override val userId: String,
        override val nickname: String,
        val gender: Int,
        val photoUrl: String,
        val token: Token,
    ) : DefaultUser(
        userId = userId,
        nickname = nickname,
        bearerToken = Token("",""),
    )
}

