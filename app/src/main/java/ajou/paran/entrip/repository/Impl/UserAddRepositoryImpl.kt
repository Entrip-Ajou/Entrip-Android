package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.WaitEntity
import ajou.paran.entrip.repository.network.UserAddRemoteSource
import ajou.paran.entrip.repository.network.dto.PushNotification
import ajou.paran.entrip.repository.network.dto.SharingFriend
import ajou.paran.entrip.repository.network.dto.UserInformation
import ajou.paran.entrip.repository.room.plan.dao.UserDao
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import kotlinx.coroutines.flow.Flow
import okhttp3.internal.wait
import javax.inject.Inject

class UserAddRepositoryImpl @Inject constructor(
    private val userAddRemoteSource: UserAddRemoteSource,
    private val userDao : UserDao
) : UserAddRepository {

    override suspend fun findAllUsersWithPlannerId(planner_id : Long) : BaseResult<List<SharingFriend>, Failure>{
        val users = userAddRemoteSource.findAllUsersWithPlannerId(planner_id)
        if(users is BaseResult.Success){
            return BaseResult.Success(users.data)
        }else{
            return BaseResult.Error(Failure((users as BaseResult.Error).err.code, users.err.message))
        }
    }

    override suspend fun searchUser(user_id_or_nickname: String): BaseResult<UserInformation, Failure> {
        val user = userAddRemoteSource.searchUser(user_id_or_nickname)
        if(user is BaseResult.Success){
            return BaseResult.Success(user.data)
        }else{
            return BaseResult.Error(Failure((user as BaseResult.Error).err.code, user.err.message))
        }
    }

    override suspend fun postNotification(notification: PushNotification, user : UserInformation): BaseResult<Unit, Failure> {
        val fcm = userAddRemoteSource.postNotification(notification)
        if(fcm is BaseResult.Success){
            val waitEntity = WaitEntity(
                nickname = user.nickname,
                photoUrl = user.photoUrl,
                token = user.token,
                planner_id = notification.data.planner_id
            )
            userDao.insertWait(waitEntity)
            return BaseResult.Success(Unit)
        }else{
            return BaseResult.Error(Failure((fcm as BaseResult.Error).err.code, fcm.err.message))
        }
    }

    fun selectWait(planner_id: Long) : Flow<List<WaitEntity>> = userDao.selectWaiting(planner_id)

    fun isExistNickname(nickname:String) : Boolean = userDao.isExistNickname(nickname)
}