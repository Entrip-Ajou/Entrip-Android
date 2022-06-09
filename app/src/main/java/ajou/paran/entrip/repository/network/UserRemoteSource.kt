package ajou.paran.entrip.repository.network

import ajou.paran.entrip.repository.network.api.UserApi
import ajou.paran.entrip.repository.network.dto.*
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import javax.inject.Inject

class UserRemoteSource
@Inject
constructor(
    private val userApi: UserApi
){
    suspend fun isExistUserId(user_id: String): BaseResponse<Boolean>
        = userApi.isExistUserId(user_id)

    suspend fun isExistNickname(nickname: String) : BaseResponse<Boolean>
        = userApi.isExistNickname(nickname)

    suspend fun saveUser(user: UserTemp) : BaseResponse<UserRequest>
        = userApi.saveUser(user)

    suspend fun updateUserToken(user_id : String, token : String) : BaseResult<Unit, Failure>{
        try{
            val res = userApi.updateToken(user_id, token)
            return if(res.status == 200) return BaseResult.Success(Unit)
            else return BaseResult.Error(Failure(res.status, res.message))
        } catch (e: NoInternetException) {
            return BaseResult.Error(Failure(0, e.message))
        } catch (e: Exception) {
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

    suspend fun getUserPlanners(user_id: String): BaseResponse<List<PlannerResponse>>
        = userApi.findAllPlanners(user_id)

    suspend fun findById(user_id : String) : BaseResponse<UserResponse>
        = userApi.findById(user_id)
}