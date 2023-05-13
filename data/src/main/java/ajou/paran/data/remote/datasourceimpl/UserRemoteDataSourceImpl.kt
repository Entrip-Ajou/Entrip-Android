package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.db.converter.toEntity
import ajou.paran.data.db.converter.toModel
import ajou.paran.data.local.datasource.PlannerRoomDataSource
import ajou.paran.data.mapper.toModel
import ajou.paran.data.remote.api.UserAPI
import ajou.paran.data.remote.datasource.UserRemoteDataSource
import ajou.paran.data.remote.model.request.SaveUserAccountRequest
import ajou.paran.data.remote.model.request.SignInByUserAccountRequest
import ajou.paran.data.remote.model.response.BaseResponse
import ajou.paran.data.utils.baseApiCall
import ajou.paran.data.utils.exceptions.NotAcceptedException
import ajou.paran.domain.model.BaseCondition
import ajou.paran.domain.model.DefaultUser
import ajou.paran.domain.model.UserSignUp
import javax.inject.Inject

class UserRemoteDataSourceImpl
@Inject
constructor(
    private val userAPI: UserAPI,
    private val plannerRoomDataSource: PlannerRoomDataSource,
) : UserRemoteDataSource {

    override suspend fun saveUserAccount(
        userId: String,
        nickname: String,
        photoUrl: String,
        gender: Long,
        password: String,
    ): UserSignUp = baseApiCall {
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
       }.toModel()
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
    ): DefaultUser = baseApiCall {
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
        }.toModel()
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
            this.data.toEntity().forEach {
                plannerRoomDataSource.insertPlanner(it.toModel())
            }
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