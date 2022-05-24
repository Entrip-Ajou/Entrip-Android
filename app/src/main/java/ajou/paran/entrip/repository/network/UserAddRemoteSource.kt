package ajou.paran.entrip.repository.network

import ajou.paran.entrip.repository.network.api.FcmApi
import ajou.paran.entrip.repository.network.api.UserApi
import ajou.paran.entrip.repository.network.dto.PushNotification
import ajou.paran.entrip.repository.network.dto.SharingFriend
import ajou.paran.entrip.repository.network.dto.UserInformation
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import android.util.Log
import javax.inject.Inject

class UserAddRemoteSource
@Inject
constructor(
    private val fcmApi: FcmApi,
    private val userApi: UserApi
) {
    companion object {
        const val TAG = "[UserAddRemoteSource]"
    }

    suspend fun findAllUsersWithPlannerId(planner_id: Long): BaseResult<List<SharingFriend>, Failure> {
        try {
            val response = userApi.findAllUsersWithPlannerId(planner_id)
            return if (response.status == 200) {
                val users = mutableListOf<SharingFriend>()
                response.data?.forEach { t ->
                    users.add(
                        SharingFriend(
                            user_id = t.userId,
                            nickname = t.nickname,
                            photoUrl = t.photoUrl
                        )
                    )
                }
                BaseResult.Success(users)
            } else {
                BaseResult.Error(Failure(response.status, response.message))
            }
        } catch (e: NoInternetException) {
            return BaseResult.Error(Failure(0, e.message))
        } catch (e: Exception) {
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

    suspend fun searchUser(user_id_or_nickname: String): BaseResult<UserInformation, Failure> {
        try {
            val response = userApi.searchUser(user_id_or_nickname)
            return if (response.status == 200) {
                val user = response.data?.let { t ->
                    UserInformation(
                        user_id = t.user_id,
                        nickname = t.nickname,
                        photoUrl = t.photoUrl,
                        token = t.token
                    )
                }
                BaseResult.Success(user!!)
            } else {
                BaseResult.Error(Failure(response.status, response.message))
            }
        } catch (e: NoInternetException) {
            return BaseResult.Error(Failure(0, e.message))
        } catch (e: Exception) {
            return BaseResult.Error(Failure(404, e.message.toString()))
        }
    }

    suspend fun postNotification(notification: PushNotification): BaseResult<Unit, Failure> {
        try {
            val response = fcmApi.postNotification(notification)
            return if (response.isSuccessful) {
                BaseResult.Success(Unit)
            } else {
                Log.e(TAG, response.raw().toString())
                BaseResult.Error(Failure(response.code(), response.errorBody().toString()))
            }
        } catch (e: NoInternetException) {
            return BaseResult.Error(Failure(0, e.message))
        } catch (e: Exception) {
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

    suspend fun addUserToPlanner(planner_id: Long, user_id: String): BaseResult<Unit, Failure> {
        try {
            val response = userApi.addPlanners(planner_id, user_id)
            return if(response.status == 200){
                BaseResult.Success(Unit)
            }else{
                BaseResult.Error(Failure(response.status, response.message))
            }
        } catch (e: NoInternetException) {
            return BaseResult.Error(Failure(0, e.message))
        } catch (e: Exception) {
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }
}