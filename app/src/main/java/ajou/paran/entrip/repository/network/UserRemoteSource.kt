package ajou.paran.entrip.repository.network

import ajou.paran.entrip.repository.network.api.UserApi
import ajou.paran.entrip.repository.network.dto.*
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import android.util.Log
import retrofit2.HttpException
import javax.inject.Inject

class UserRemoteSource
@Inject
constructor(
    private val userApi: UserApi
) {
    companion object {
        const val TAG = "[UserRemote]"
    }

    suspend fun isExistUserId(user_id: String): BaseResponse<Boolean>
    = userApi.isExistUserId(user_id)

    suspend fun isExistNickname(nickname: String): BaseResponse<Boolean>
    = userApi.isExistNickname(nickname)

    suspend fun saveUser(user: UserTemp): BaseResponse<UserRequest>
    = userApi.saveUser(user)

    suspend fun updateUserToken(user_id: String, token: String): BaseResult<Unit, Failure>
    = try {
        val res = userApi.updateToken(user_id, token)
        when(res.status) {
            200 -> { BaseResult.Success(Unit) }
            else -> {
                Log.e(CommentRemoteSource.TAG, "Err code = ${res.status}, Err message = ${res.message}")
                BaseResult.Error(Failure(res.status, res.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(CommentRemoteSource.TAG, "NoInternetException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(CommentRemoteSource.TAG, "HttpException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(e.code(), e.message.toString()))
    } catch (e: Exception) {
        Log.e(CommentRemoteSource.TAG, "Exception Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun getUserPlanners(user_id: String): BaseResponse<List<PlannerResponse>>
    = userApi.findAllPlanners(user_id)

    suspend fun findById(user_id: String): BaseResponse<UserResponse>
    = userApi.findById(user_id)

    suspend fun findByUserId(user_id: String): BaseResponse<UserResponse>
    = userApi.findByUserId(user_id)

    suspend fun getListTrip(travelFavorite: String?): BaseResponse<List<TripResponse>>
    = userApi.findTrip(travelFavorite)

}