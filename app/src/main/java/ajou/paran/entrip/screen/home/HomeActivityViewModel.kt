package ajou.paran.entrip.screen.home

import ajou.paran.entrip.model.InviteEntity
import ajou.paran.entrip.repository.Impl.PlannerRepositoryImpl
import ajou.paran.entrip.repository.Impl.UserAddRepositoryImpl
import ajou.paran.entrip.repository.network.dto.PushNotification
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
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
     *  1) planner가 존재 하는지 확인 -> 수락을 눌러도, 해당 플래너가 서버 상에 존재하지 않을 수도 있다
     *  -> plannerIsExistWithId(planner_id)
     *
     *  2-1) 존재하지 않을 경우 : 삭제된 플래너 Err message -> 종료
     *
     *  2-2) 존재할 경우 : 초대를 보낸 사용자가 해당 플래너에 있는지 확인
     *  -> 초대를 보낸 사용자가 해당 플래너에 있는지 확인
     *  -> userIsExistWithPlanner(planner_id, user_id)
     *
     *  3) 존재할 경우 notification 전송 / 존재하지 않을 때 종료(알림을 보낼 필요가 없다)
     *
     */

    fun acceptInvitation(inviteEntity : InviteEntity, user_id : String, notificationData: PushNotification){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            /*
            val res1 = userAddRepositoryImpl.addUserToPlanner(inviteEntity.planner_id, user_id)
            when(res1){
                is BaseResult.Success -> {
                    val res2 = plannerRepository.acceptInvitation(inviteEntity.planner_id)
                    when(res2){
                        is BaseResult.Success -> {
                            val res3 = userAddRepositoryImpl.postNotification(notificationData, inviteEntity)
                            when(res3){
                                is BaseResult.Success -> _state.value = ApiState.Success(res3.data)
                                is BaseResult.Error -> _state.value = ApiState.Failure(res3.err.code)
                            }
                        }
                        is BaseResult.Error -> _state.value = ApiState.Failure(res2.err.code)
                    }
                }
                is BaseResult.Error -> _state.value = ApiState.Failure(res1.err.code)
            }
            */
            val res1 = plannerRepository.isExist(inviteEntity.planner_id)
            when(res1){
                is BaseResult.Success -> {
                    // 플래너가 존재할 경우
                    if(res1.data){
                        // todo : userIsExistWithPlanner 실패일 경우 HttpException이 발생해서 실패의 경우를 처리할 수 없으므로 동환님이 추가로 작업해주시면 적용하기

                    }else{
                        // 플래너가 존재하지 않을 경우 => 이미 삭제된 플래너 이므로 code 번호는 500으로 설정
                        _state.value = ApiState.Failure(500)
                    }
                }
                // res1에 대한 예외(Network, NoInternet, Exception, ... )
                is BaseResult.Error -> _state.value = ApiState.Failure(res1.err.code)
            }
            delay(500)
            hideLoading()
        }
    }


    fun rejectInvitation(inviteEntity: InviteEntity, notificationData: PushNotification){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = userAddRepositoryImpl.postNotification(notificationData, inviteEntity)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(res.data)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }
}