package ajou.paran.entrip.repository.network

import ajou.paran.entrip.repository.network.api.UserApi
import ajou.paran.entrip.repository.network.dto.BaseResponse
import ajou.paran.entrip.repository.network.dto.UserRequest
import ajou.paran.entrip.repository.network.dto.UserResponse
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

    suspend fun saveUser(user: UserResponse) : BaseResponse<UserRequest>
        = userApi.saveUser(user)
}