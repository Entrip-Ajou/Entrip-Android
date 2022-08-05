package ajou.paran.entrip.screen.planner.top

import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.model.WebSocketMessage
import ajou.paran.entrip.repository.Impl.PlannerRepositoryImpl
import ajou.paran.entrip.repository.network.dto.PlannerUpdateRequest
import ajou.paran.entrip.util.network.BaseResult
import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import java.net.SocketException
import javax.inject.Inject

@HiltViewModel
class PlannerActivityViewModel
@Inject
constructor(
    private val plannerRepository: PlannerRepositoryImpl,
    val stompClient : StompClient,
    val sharedPreferences : SharedPreferences
) : ViewModel() {
    companion object{
        private const val TAG = "[PlannerActViewModel]"
    }

    val intervalMills = 1000
    private val gson = Gson()

    private val _state = MutableStateFlow<PlannerState>(PlannerState.Init)
    val state : StateFlow<PlannerState> get() = _state

    val user_id = sharedPreferences.getString("user_id", null)?.toString()

    fun setLoading() {
        _state.value = PlannerState.IsLoading(true)
    }

    fun hideLoading() {
        _state.value = PlannerState.IsLoading(false)
    }

    private fun setUpdate(){
        _state.value = PlannerState.IsUpdate(true)
    }

    private fun setNotUpdate(){
        _state.value = PlannerState.IsUpdate(false)
    }

    fun getFlowPlanner(plannerId : Long): Flow<PlannerEntity> = plannerRepository.getFlowPlanner(plannerId)

    fun plannerChange(list: List<PlannerDate>, planner : PlannerEntity){
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val fetch = plannerRepository.findPlanner(planner.planner_id)
            if(fetch is BaseResult.Success){
                val res = plannerRepository.updatePlanner(planner.planner_id, planner.let { t ->
                    PlannerUpdateRequest(
                        title = t.title,
                        start_date = list.first().date,
                        end_date = list.last().date
                    )
                })
                when(res){
                    is BaseResult.Success -> {
                        _state.value = PlannerState.Success(Unit)
                        getFlowPlanner(planner.planner_id)
                        sendPlannerChangeMessage(user_id!!, 0, planner.planner_id)
                    }
                    is BaseResult.Error -> _state.value = PlannerState.Failure(res.err.code)
                }
            }else{
                _state.value = PlannerState.Failure((fetch as BaseResult.Error).err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun plannerChange(title: String, planner : PlannerEntity){
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val fetch = plannerRepository.findPlanner(planner.planner_id)
            if(fetch is BaseResult.Success){
                val res = plannerRepository.updatePlanner(planner.planner_id, planner.let { t ->
                    PlannerUpdateRequest(
                        title = title,
                        start_date = t.start_date,
                        end_date = t.end_date
                    )
                })
                when(res){
                    is BaseResult.Success -> {
                        _state.value = PlannerState.Success(Unit)
                        getFlowPlanner(planner.planner_id)
                        sendPlannerChangeMessage(user_id!!, 0, planner.planner_id)
                    }
                    is BaseResult.Error -> _state.value = PlannerState.Failure(res.err.code)
                }
            }else{
                _state.value = PlannerState.Failure((fetch as BaseResult.Error).err.code)
            }

            delay(500)
            hideLoading()
        }
    }

    fun deletePlanner(user_id : String, planner_id : Long){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = plannerRepository.deletePlanner(user_id, planner_id)
            when(res){
                is BaseResult.Success -> _state.value = PlannerState.Success(res.data)
                is BaseResult.Error -> _state.value = PlannerState.Failure(res.err.code)
            }

            delay(500)
            hideLoading()
        }
    }

    /**
     * syncRemoteDB & observeTimeStamp => Polling 방식을 Websocket으로 대체할 예정


    fun syncRemoteDB(planner_id: Long) {
        job = viewModelScope.launch(Dispatchers.IO) {
            plannerRepository.syncRemoteDB(planner_id)
                .catch { e ->
                    Log.e(TAG, "Error message = " + e.message)
                }
                .collect { res ->
                    when (res) {
                        is BaseResult.Success -> {
                            lastTimeStamp = res.data as String
                        }
                        is BaseResult.Error -> {
                            _state.value = PlannerState.Failure(500)
                        }
                    }
                }
        }
    }

    fun observeTimeStamp(planner_id: Long){
        runBlocking{
            job.join()
        }
        viewModelScope.launch{
            plannerRepository.latestTimeStamp(planner_id).collectLatest{
                when {
                    //it == "NotExist" -> _state.value = PlannerState.Failure(500)
                    it == "NoInternet" -> _state.value = PlannerState.Failure(0)
                    lastTimeStamp != it -> setUpdate()
                    else -> setNotUpdate()
                }
            }
        }
    }
    */



    fun runStomp(planner_id : Long) {
        stompClient.withServerHeartbeat(intervalMills).withClientHeartbeat(intervalMills)

        stompClient.lifecycle().subscribe{ lifecycleEvent ->
            when(lifecycleEvent.type){
                LifecycleEvent.Type.OPENED -> {
                    Log.i(TAG, "OPEND!!")
                }
                LifecycleEvent.Type.CLOSED -> {
                    Log.i(TAG, "CLOSED!!")
                }
                LifecycleEvent.Type.ERROR -> {
                    Log.i(TAG, "ERROR!!")
                    Log.e(TAG, "CONNECT ERROR "+lifecycleEvent.exception.toString())

                    when(lifecycleEvent.exception.cause){
                        is SocketException -> {

                        }
                    }
                }

                LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                    Log.i(TAG, "FAILED_SERVER_HEARTBEAT")
                }

                else ->{
                    Log.i(TAG, "ELSE "+lifecycleEvent.message)
                }
            }
        }

        // 메시지를 받을 때
        stompClient.topic("/topic/public/"+planner_id).subscribe{ topicMessage ->
            Log.i("[WebSocket]","message Receive" + topicMessage.payload)
            val messageDto = gson.fromJson(topicMessage.payload, WebSocketMessage::class.java)
            Log.d("[WebSocket]", "content = " + messageDto.content)
            Log.d("[WebSocket]", "sender = " + messageDto.sender)
            Log.d("[WebSocket]", "type = " + messageDto.type)
            Log.d("[WebSocket]", "planner_id = " + messageDto.planner_id)
            Log.d("[WebSocket]", "date = " + messageDto.date)

            if(user_id != messageDto.sender){
                when(messageDto.content){
                    0 -> {
                        Log.d("[WebSocket]", "Planner_id " + planner_id+" Sync 작업 시작")
                        fetchPlanner(planner_id)
                    }

                    1 -> {
                        Log.d("[WebSocket]", "Date = " + messageDto.date +"에 해당하는 plan들 Sync 작업 시작")
                    }
                }
            }
        }
        stompClient.connect()
    }

    fun fetchPlanner(planner_id : Long){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = plannerRepository.fetchPlanner(planner_id)
            when(res){
                is BaseResult.Success -> _state.value = PlannerState.Success(res.data)
                is BaseResult.Error -> _state.value = PlannerState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    // todo : planner update하는 부분에 넣으면 된다
    fun sendPlannerChangeMessage(sender : String, content : Int, planner_id : Long){
        val data = JSONObject()
        data.put("type", "CHAT")
        data.put("content", content)
        data.put("sender", sender)
        data.put("planner_id", planner_id)
        data.put("date", null)
        stompClient.send("/app/chat.sendMessage", data.toString()).subscribe()
        Log.e("[WebSocket]", "update가 일어나서 Message 전송 + Planner_id : "+planner_id)
    }
}

sealed class PlannerState {
    object Init : PlannerState()
    data class IsLoading(val isLoading: Boolean) : PlannerState()
    data class IsUpdate(val isUpdate: Boolean) : PlannerState()
    data class Success(val data : Any) : PlannerState()
    data class Failure(val code : Int) : PlannerState()
}