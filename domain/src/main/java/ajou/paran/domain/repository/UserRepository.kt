package ajou.paran.domain.repository

interface UserRepository {

    suspend fun isExistUser(
        userId: String
    )


}