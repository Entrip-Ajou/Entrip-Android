package ajou.paran.data.repository

import ajou.paran.data.remote.datasource.UserRemoteDataSource
import ajou.paran.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl
@Inject
constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun isExistUser(
        userId: String
    ) {
        userRemoteDataSource.isExistUserByUserId(
            userId = userId
        )
    }

}