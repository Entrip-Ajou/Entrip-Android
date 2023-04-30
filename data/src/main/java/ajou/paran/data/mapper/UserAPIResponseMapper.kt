package ajou.paran.data.mapper

import ajou.paran.data.remote.model.response.BaseResponse
import ajou.paran.data.remote.model.response.SaveUserAccountResponse
import ajou.paran.data.remote.model.response.SignInByUserAccountResponse
import ajou.paran.domain.model.BaseCondition
import ajou.paran.domain.model.DefaultUser
import ajou.paran.domain.model.Token
import ajou.paran.domain.model.UserSignUp

internal fun BaseResponse<Boolean>.toModel(): BaseCondition = BaseCondition(data)

internal fun BaseResponse<SaveUserAccountResponse>.toModel(): UserSignUp = data.toModel()

internal fun BaseResponse<SignInByUserAccountResponse>.toModel(): DefaultUser = data.toModel()

internal fun SaveUserAccountResponse.toModel() = UserSignUp(
    userId = userId,
    nickname = nickname,
    gender = gender,
    photoUrl = photoUrl
)

internal fun SignInByUserAccountResponse.toModel() = DefaultUser(
    userId = userId,
    nickname = nickname,
    bearerToken = Token(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
)