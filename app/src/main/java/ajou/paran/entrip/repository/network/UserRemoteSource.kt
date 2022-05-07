package ajou.paran.entrip.repository.network

import ajou.paran.entrip.repository.network.api.UserApi
import ajou.paran.entrip.repository.network.dto.BaseResponse

class UserRemoteSource
constructor(
    private val userApi: UserApi
){
    suspend fun findUserId(user_id: String): BaseResponse<Boolean>
        = userApi.findUserId(user_id)

    suspend fun checkNickName(nickName: String) : BaseResponse<Boolean>
        = userApi.checkNickName(nickName)

    suspend fun insertNickName(user_id: String, nickName: String) : BaseResponse<Int>
        = userApi.insertNickName(user_id, nickName)
}