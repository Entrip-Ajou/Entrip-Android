package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.InviteEntity
import ajou.paran.entrip.model.WaitEntity
import ajou.paran.entrip.repository.network.dto.PushNotification
import ajou.paran.entrip.repository.network.dto.SharingFriend
import ajou.paran.entrip.repository.network.dto.UserInformation
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import kotlinx.coroutines.flow.Flow

interface UserAddRepository {
    suspend fun searchUser(user_id_or_nickname : String) : BaseResult<UserInformation, Failure>
    suspend fun postNotification(notification: PushNotification, user : UserInformation) : BaseResult<Unit, Failure>
    suspend fun postNotification(notification : PushNotification, inviteEntity: InviteEntity) : BaseResult<Unit, Failure>
    suspend fun findAllUsersWithPlannerId(planner_id : Long) : BaseResult<List<SharingFriend>, Failure>
    suspend fun addUserToPlanner(planner_id : Long, user_id : String) : BaseResult<Unit, Failure>
    suspend fun userIsExistWithPlanner(planner_id : Long, user_id : String) : BaseResult<Unit, Failure>
    suspend fun userNickNameIsExistWithPlanner(planner_id : Long, nickname : String) : BaseResult<Unit, Failure>
}