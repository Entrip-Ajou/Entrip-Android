package ajou.paran.domain.repository

import ajou.paran.domain.model.DefaultUser
import ajou.paran.domain.model.UserSignUp

interface UserRepository {

    suspend fun saveUserAccount(
        userId: String,
        nickname: String,
        photoUrl: String,
        gender: Long,
        password: String,
    ): UserSignUp

    suspend fun signInByUserAccount(
        userId: String,
        password: String
    ): DefaultUser

    suspend fun isExistUser(
        userId: String
    ): Boolean

    suspend fun isExistNickname(
        nickname: String
    ): Boolean

    suspend fun fetchIsEntry(): Boolean

    suspend fun saveIsEntry(
        isEntry: Boolean
    )

    suspend fun clearIsEntry()
}