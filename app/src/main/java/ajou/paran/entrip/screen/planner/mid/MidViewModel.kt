package ajou.paran.entrip.screen.planner.mid

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.repository.Impl.PlanRepositoryImpl
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import ua.naiksoftware.stomp.StompClient
import javax.inject.Inject

@HiltViewModel
class MidViewModel @Inject constructor(
    private val planRepository: PlanRepositoryImpl,
    private val stompClient: StompClient,
    private val sharedPreferences : SharedPreferences
) : ViewModel() {
    companion object {
        private const val TAG = "[MidViewModel]"
    }

    private val user_id = sharedPreferences.getString("user_id", null)?.toString()

    private val _state = MutableStateFlow<ApiState>(ApiState.Init)
    val state: StateFlow<ApiState> get() = _state

    fun setLoading() {
        _state.value = ApiState.IsLoading(true)
    }

    fun hideLoading() {
        _state.value = ApiState.IsLoading(false)
    }

    fun loadPlan(date: String, plannerId: Long): Flow<List<PlanEntity>> =
        planRepository.selectPlan(date, plannerId)

    fun deletePlan(plan_id: Long, planner_id: Long, date : String) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = planRepository.deletePlan(plan_id, planner_id)
            if(res is BaseResult.Success) {
                _state.value = ApiState.Success(Unit)
                sendPlanChangeMessage(user_id!!, 1, planner_id, date)
            }
            else _state.value = ApiState.Failure((res as BaseResult.Error).err.code)
            delay(500)
            hideLoading()
        }
    }

    fun sendPlanChangeMessage(sender : String, content : Int, planner_id : Long, date: String){
        val data = JSONObject()
        data.put("type", "CHAT")
        data.put("content", content)
        data.put("sender", sender)
        data.put("planner_id", planner_id)
        data.put("date", date)
        stompClient.send("/app/chat.sendMessage", data.toString()).subscribe()

        Log.e("[WebSocket]", "<Plan update>  + Planner_id : "+planner_id)
    }
}
