package ajou.paran.data.remote.datasource

import ajou.paran.domain.model.BaseCondition
import ajou.paran.domain.model.DefaultUser
import ajou.paran.domain.model.UserSignUp

interface UserRemoteDataSource {

    suspend fun saveUserAccount(
        userId: String,
        nickname: String,
        photoUrl: String,
        gender: Long,
        password: String
    ): UserSignUp

    suspend fun signInByUserAccount(
        userId: String,
        password: String
    ): DefaultUser.User

    suspend fun isExistUserByUserId(
        userId: String
    ): BaseCondition

    suspend fun isExistUserByNickname(
        nickname: String
    ): BaseCondition

    suspend fun updateToken(
        userId : String,
        token : String
    ): Unit

    suspend fun searchUser(
        userIdOrNickname : String
    ): Unit

    suspend fun findAllUsersByPlannerId(
        plannerId : Long
    ): Unit

    suspend fun addPlanners(
        plannerId : Long,
        userId : String
    ): Unit

    suspend fun findAllPlannersByUser(
        userId: String
    ): Unit

    suspend fun findUserById(
        userId : String
    ): Unit

    suspend fun isExistUserInPlannerByUserId(
        plannerId : Long,
        userId : String
    ): Unit

    suspend fun isExistUserInPlannerByNickname(
        plannerId : Long,
        nickname : String
    ): Unit

}