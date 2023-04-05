package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.remote.api.UserAPI
import ajou.paran.data.remote.datasource.UserRemoteDataSource
import javax.inject.Inject

class UserRemoteDataSourceImpl
@Inject
constructor(
    private val userAPI: UserAPI,
) : UserRemoteDataSource {

    override suspend fun saveUserAccount(
        userId: String,
        nickname: String,
        photoUrl: String,
        token: String,
        gender: Long,
        password: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun signInByUserAccount(
        userId: String,
        password: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun isExistUserByUserId(
        userId: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun isExistUserByNickname(
        nickname: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(
        userId: String,
        gender: Int,
        nickname: String,
        photoUrl: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateToken(
        userId: String,
        token: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun searchUser(
        userIdOrNickname: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun findAllUsersByPlannerId(
        plannerId: Long
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun addPlanners(
        plannerId: Long,
        userId: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun findAllPlannersByUser(
        userId: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun findUserById(
        userId: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun isExistUserInPlannerByUserId(
        plannerId: Long,
        userId: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun isExistUserInPlannerByNickname(
        plannerId: Long,
        nickname: String
    ) {
        TODO("Not yet implemented")
    }

}