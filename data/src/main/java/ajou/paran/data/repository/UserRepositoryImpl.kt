package ajou.paran.data.repository

import ajou.paran.data.local.datasource.DataStoreDataSource
import ajou.paran.data.remote.datasource.UserRemoteDataSource
import ajou.paran.domain.model.DefaultUser
import ajou.paran.domain.model.UserSignUp
import ajou.paran.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl
@Inject
constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val dataStoreDataSource: DataStoreDataSource
) : UserRepository {

    override suspend fun saveUserAccount(
        userId: String,
        nickname: String,
        photoUrl: String,
        gender: Long,
        password: String
    ): UserSignUp = userRemoteDataSource.saveUserAccount(
        userId = userId,
        nickname = nickname,
        photoUrl = photoUrl,
        gender = gender,
        password = password
    )

    override suspend fun signInByUserAccount(
        userId: String,
        password: String
    ): DefaultUser = userRemoteDataSource.signInByUserAccount(
        userId,
        password
    ).also { user ->
        dataStoreDataSource.saveAccessToken(user.bearerToken.accessToken)
        dataStoreDataSource.saveRefreshToken(user.bearerToken.refreshToken)
    }

    override suspend fun isExistUser(
        userId: String
    ): Boolean = userRemoteDataSource.isExistUserByUserId(
        userId = userId
    ).condition

    override suspend fun isExistNickname(
        nickname: String
    ): Boolean = userRemoteDataSource.isExistUserByNickname(
        nickname = nickname
    ).condition

}