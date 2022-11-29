package ajou.paran.entrip.screen.home

import ajou.paran.entrip.model.InviteEntity
import ajou.paran.entrip.repository.Impl.PlannerRepositoryImpl
import ajou.paran.entrip.repository.Impl.UserAddRepositoryImpl
import ajou.paran.entrip.repository.network.dto.PushNotification
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeActivityViewModel
@Inject
constructor(
    private val plannerRepository: PlannerRepositoryImpl,
    private val userAddRepositoryImpl: UserAddRepositoryImpl
)
: ViewModel() {
    companion object{
        const val TAG = "[HomeActivityVM]"
    }

    private val _state = MutableStateFlow<ApiState>(ApiState.Init)
    val state : StateFlow<ApiState> get() = _state

    fun setLoading() {
        _state.value = ApiState.IsLoading(true)
    }

    fun hideLoading() {
        _state.value = ApiState.IsLoading(false)
    }

    fun selectAllInvite() = userAddRepositoryImpl.selectInvite()

    fun countInvite() = userAddRepositoryImpl.countInvite()

    /** Accept logic
     *  1) planner가 존재 하는지 확인 -> 수락을 눌러도, 해당 플래너가 서버 상에 존재하지 않을 수도 있다.
     *
     *  2-1) 존재하지 않을 경우(http status code : 202) : 삭제된 플래너(500으로 변경) Err message -> 종료
     *
     *  2-2) 존재할 경우 : 해당 플래너 Fetch 작업 필요 (addUserToPlanner -> acceptInvitation)
     *
     *  2-3) 그 외 Exception : NoInternet, Server Internal Error, ...
     *
     *  3) 초대를 보낸 user가 planner에 있는지 확인하기
     *
     *  4-1) 있는 경우 : Notification 전송
     *
     *  4-2) 없는 경우 : 종료
     *
     */

    fun acceptInvitation(inviteEntity : InviteEntity, user_id : String, notificationData: PushNotification){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            when(val res1 = plannerRepository.isExist(inviteEntity.planner_id)){
                is BaseResult.Success -> {
                    when(val res2 = userAddRepositoryImpl.addUserToPlanner(inviteEntity.planner_id, user_id)){
                        is BaseResult.Success -> {
                            val res3 = plannerRepository.acceptInvitation(inviteEntity.planner_id)
                            when(res3){
                                is BaseResult.Success -> {
                                    val res4 = userAddRepositoryImpl.userIsExistWithPlanner(inviteEntity.planner_id, inviteEntity.user_id)
                                    when(res4){
                                        is BaseResult.Success -> {
                                            val res5 = userAddRepositoryImpl.postNotification(notificationData, inviteEntity)
                                            when(res5){
                                                is BaseResult.Success -> _state.value = ApiState.Success(Unit)
                                                is BaseResult.Error -> _state.value = ApiState.Failure(res5.err.code)
                                            }
                                        }
                                        is BaseResult.Error -> {
                                            _state.value = ApiState.Failure(res4.err.code)
                                        }
                                    }
                                }
                                is BaseResult.Error -> _state.value = ApiState.Failure(res3.err.code)
                            }
                        }
                        is BaseResult.Error -> {
                            _state.value = ApiState.Failure(res2.err.code)
                        }
                    }
                }
                is BaseResult.Error -> {
                    when(res1.err.code){
                        202 -> {
                            plannerRepository.deleteInvitation(inviteEntity)
                            _state.value = ApiState.Failure(204)
                        }
                        else -> _state.value = ApiState.Failure(res1.err.code)
                    }
                }
            }
            delay(500)
            hideLoading()
        }
    }

    fun rejectInvitation(inviteEntity: InviteEntity, notificationData: PushNotification){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            when(val res1 = userAddRepositoryImpl.userIsExistWithPlanner(inviteEntity.planner_id, inviteEntity.user_id)){
                is BaseResult.Success -> {
                    when(val res2 = userAddRepositoryImpl.postNotification(notificationData, inviteEntity)){
                        is BaseResult.Success -> _state.value = ApiState.Success(Unit)
                        is BaseResult.Error -> _state.value = ApiState.Failure(res2.err.code)
                    }
                }
                is BaseResult.Error -> {
                    when(res1.err.code){
                        500, 202 -> {
                            // 플래너가 이미 삭제되었거나, 사용자가 없는 경우 -> 성공으로 설정한 뒤 종료
                            plannerRepository.deleteInvitation(inviteEntity)
                            _state.value = ApiState.Success(Unit)
                        }
                        else -> _state.value = ApiState.Failure(res1.err.code)
                    }
                }
            }
            delay(500)
            hideLoading()
        }
    }

}