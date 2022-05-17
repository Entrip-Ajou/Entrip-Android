package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.UserAddRemoteSource
import ajou.paran.entrip.repository.room.plan.dao.UserDao
import javax.inject.Inject

class UserAddRepositoryImpl @Inject constructor(
    private val userAddRemoteSource: UserAddRemoteSource,
    private val userDao : UserDao
) : UserAddRepository {

}