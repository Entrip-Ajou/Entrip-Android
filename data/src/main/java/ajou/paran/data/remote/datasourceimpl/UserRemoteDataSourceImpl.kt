package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.remote.api.UserAPI
import ajou.paran.data.remote.datasource.UserRemoteDataSource
import ajou.paran.data.remote.model.request.SaveUserAccountRequest
import ajou.paran.data.utils.baseApiCall
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
        password: String,
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
//        userAPI.saveUserAccount(
//            request = SaveUserAccountRequest(
//                userId = userId,
//                nickname = nickname,
//                photoUrl = photoUrl,
//                token = token,
//                gender = gender,
//                password = password
//            )
//        )
    }

    override suspend fun signInByUserAccount(
        userId: String,
        password: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun isExistUserByUserId(
        userId: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun isExistUserByNickname(
        nickname: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun updateToken(
        userId: String,
        token: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun searchUser(
        userIdOrNickname: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun findAllUsersByPlannerId(
        plannerId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun addPlanners(
        plannerId: Long,
        userId: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun findAllPlannersByUser(
        userId: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun findUserById(
        userId: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun isExistUserInPlannerByUserId(
        plannerId: Long,
        userId: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun isExistUserInPlannerByNickname(
        plannerId: Long,
        nickname: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

}