package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.WaitEntity
import ajou.paran.entrip.repository.network.dto.PushNotification
import ajou.paran.entrip.repository.network.dto.SharingFriend
import ajou.paran.entrip.repository.network.dto.UserInformation
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import kotlinx.coroutines.flow.Flow

interface UserAddRepository {
    suspend fun searchUser(user_id_or_nickname : String) : BaseResult<UserInformation, Failure>
    suspend fun postNotification(notification: PushNotification, user : UserInformation) : BaseResult<Flow<List<WaitEntity>>, Failure>
    suspend fun findAllUsersWithPlannerId(planner_id : Long) : BaseResult<List<SharingFriend>, Failure>
}