package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.mapper.toModel
import ajou.paran.data.remote.api.UserAPI
import ajou.paran.data.remote.datasource.UserRemoteDataSource
import ajou.paran.data.remote.model.request.SaveUserAccountRequest
import ajou.paran.data.remote.model.request.SignInByUserAccountRequest
import ajou.paran.data.remote.model.response.BaseResponse
import ajou.paran.data.utils.baseApiCall
import ajou.paran.data.utils.exceptions.NotAcceptedException
import ajou.paran.domain.model.BaseCondition
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
        gender: Long,
        password: String,
    ): Unit = baseApiCall {
       userAPI.saveUserAccount(
           request = SaveUserAccountRequest(
               userId = userId,
               nickname = nickname,
               photoUrl = photoUrl,
               gender = gender,
               password = password
           )
       ).apply {
//           when (statusCode) {
//               202 -> {}
//               else -> {}
//           }
       }
    }

    override suspend fun isExistUserByUserId(
        userId: String
    ): BaseCondition = baseApiCall {
        userAPI.isExistUserByUserId(
            userId = userId
        ).apply {
            // Without Exception Handle Because data is false
//            when (statusCode) {
//                202 -> {}
//                else -> {}
//            }
        }.toModel()
    }

    override suspend fun isExistUserByNickname(
        nickname: String
    ): BaseCondition = baseApiCall {
        userAPI.isExistUserByNickname(
            nickname = nickname
        ).apply {
            // Without Exception Handle Because data is false
//            when (statusCode) {
//                202 -> {}
//                else -> {}
//            }
        }.toModel()
    }

    override suspend fun signInByUserAccount(
        userId: String,
        password: String
    ): Unit = baseApiCall {
        userAPI.signInByUserAccount(
            request = SignInByUserAccountRequest(
                userId = userId,
                password = password
            )
        ).apply {
            when (statusCode) {
                202 -> {
                    when (data.detailedMessage) {
                        NotAcceptedException.FAIL_BY_ID,
                        NotAcceptedException.FAIL_BY_PASSWORD, -> throw NotAcceptedException()
                    }
                }
            }
        }
    }

    override suspend fun updateToken(
        userId: String,
        token: String
    ): Unit = baseApiCall {
        userAPI.updateToken(
            userId = userId,
            token = token,
        ).apply {
//            when (statusCode) {
//                202 -> {
//
//                }
//            }
        }
    }

    override suspend fun searchUser(
        userIdOrNickname: String
    ): Unit = baseApiCall {
        userAPI.searchUser(
            userIdOrNickname = userIdOrNickname
        ).apply {
            when (statusCode) {
                202 -> throw NotAcceptedException()
            }
        }
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
        userAPI.findAllPlannersByUser(
            userId = userId
        ).apply {
//
        }
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