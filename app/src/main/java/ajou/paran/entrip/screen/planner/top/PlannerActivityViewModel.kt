package ajou.paran.entrip.screen.planner.top

import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.model.WebSocketMessage
import ajou.paran.entrip.repository.Impl.PlannerRepositoryImpl
import ajou.paran.entrip.repository.network.dto.PlannerUpdateRequest
import ajou.paran.entrip.util.network.BaseResult
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.json.JSONObject
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import javax.inject.Inject

@HiltViewModel
class PlannerActivityViewModel
@Inject
constructor(
    private val plannerRepository: PlannerRepositoryImpl,
    val stompClient: StompClient,
    private val sharedPreferences: SharedPreferences,
    application : Application
) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "[PlannerActViewModel]"
    }

    private val gson = Gson()
    private val context = getApplication<Application>().applicationContext

    val userId: String
    get() = sharedPreferences.getString("user_id", null) ?: ""

    private val _state = MutableStateFlow<PlannerState>(PlannerState.Init)
    val state: StateFlow<PlannerState> get() = _state

    fun setLoading() {
        _state.value = PlannerState.IsLoading(true)
    }

    fun hideLoading() {
        _state.value = PlannerState.IsLoading(false)
    }

    fun getFlowPlanner(plannerId: Long): Flow<PlannerEntity> =
        plannerRepository.getFlowPlanner(plannerId)

    fun plannerChange(list: List<PlannerDate>, planner: PlannerEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = plannerRepository.updatePlanner(planner.planner_id, planner.let { t ->
                PlannerUpdateRequest(
                    title = t.title,
                    start_date = list.first().date,
                    end_date = list.last().date
                )
            })
            when (res) {
                is BaseResult.Success -> {
                    _state.value = PlannerState.Success(Unit)
                    getFlowPlanner(planner.planner_id)
                    sendPlannerChangeMessage(userId, 0, planner.planner_id)
                }
                is BaseResult.Error -> _state.value = PlannerState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun plannerChange(title: String, planner: PlannerEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = plannerRepository.updatePlanner(planner.planner_id, planner.let { t ->
                PlannerUpdateRequest(
                    title = title,
                    start_date = t.start_date,
                    end_date = t.end_date
                )
            })
            when (res) {
                is BaseResult.Success -> {
                    _state.value = PlannerState.Success(Unit)
                    getFlowPlanner(planner.planner_id)
                    sendPlannerChangeMessage(userId, 0, planner.planner_id)
                }
                is BaseResult.Error -> _state.value = PlannerState.Failure(res.err.code)
            }

            delay(500)
            hideLoading()
        }
    }

    fun deletePlanner(planner_id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = plannerRepository.deletePlanner(userId, planner_id)
            when (res) {
                is BaseResult.Success -> _state.value = PlannerState.Success(res.data)
                is BaseResult.Error -> _state.value = PlannerState.Failure(res.err.code)
            }

            delay(500)
            hideLoading()
        }
    }

    @Synchronized fun runStomp(planner_id: Long) {
        stompClient.topic("/topic/public/" + planner_id).subscribe { topicMessage ->
            Log.i("[WebSocket]", "message Receive" + topicMessage.payload)
            val messageDto = gson.fromJson(topicMessage.payload, WebSocketMessage::class.java)

            if (userId != messageDto.sender) {
                when (messageDto.content) {
                    0 -> {
                        fetchPlanner(planner_id)
                    }

                    1 -> {
                        val date = messageDto.date.substring(0, 4) + messageDto.date.substring(5, 7) + messageDto.date.substring(8, 10)
                        fetchPlan(planner_id, date)
                    }

                    2 -> {
                        plannerRepository.updateIsExistComments(messageDto.isExistComments, messageDto.plan_id)
                    }
                }
            }
        }
        stompClient.connect()

        stompClient.lifecycle().subscribe{ lifecycleEvent ->
            when(lifecycleEvent.type){
                LifecycleEvent.Type.OPENED -> {
                    Log.i(TAG, "OPEND!!")
                }
                LifecycleEvent.Type.CLOSED -> {
                    Log.i(TAG, "CLOSED!!")
//                    CoroutineScope(Dispatchers.Default).launch{
//                        while(true){
//                            if(isConnected()) {
//                                //reconnectStomp(planner_id)
//                                break;
//                            }else delay(1000)
//                        }
//                    }
                }
                LifecycleEvent.Type.ERROR -> {
                    Log.i(TAG, "ERROR!!")
                    Log.e(TAG, "CONNECT ERROR "+lifecycleEvent.exception.toString())
                }

                LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                    Log.i(TAG, "FAILED_SERVER_HEARTBEAT")
                }

                else ->{
                    Log.i(TAG, "ELSE "+lifecycleEvent.message)
                }
            }
        }
    }

    @Synchronized fun reconnectStomp(planner_id : Long){
        stompClient.topic("/topic/public/" + planner_id).subscribe { topicMessage ->
            Log.i("[WebSocket]", "message Receive" + topicMessage.payload)
            val messageDto = gson.fromJson(topicMessage.payload, WebSocketMessage::class.java)

            if (userId != messageDto.sender) {
                when (messageDto.content) {
                    0 -> {
                        fetchPlanner(planner_id)
                    }

                    1 -> {
                        val date = messageDto.date.substring(0, 4) + messageDto.date.substring(5, 7) + messageDto.date.substring(8, 10)
                        fetchPlan(planner_id, date)
                    }

                    2 -> {
                        plannerRepository.updateIsExistComments(messageDto.isExistComments, messageDto.plan_id)
                    }
                }
            }
        }
        stompClient.connect()
    }

    private fun isConnected() : Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network = connectivityManager.activeNetwork ?: return false
            val actNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when{
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }else{
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    fun fetchPlanner(planner_id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = plannerRepository.fetchPlanner(planner_id)
            when (res) {
                is BaseResult.Success -> _state.value = PlannerState.Success(res.data)
                is BaseResult.Error -> _state.value = PlannerState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun fetchPlan(planner_id: Long, date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = plannerRepository.findByPlannerIdWithDate(planner_id, date)
            when (res) {
                is BaseResult.Success -> _state.value = PlannerState.Success(res.data)
                is BaseResult.Error -> _state.value = PlannerState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun sendPlannerChangeMessage(sender: String, content: Int, planner_id: Long) {
        val data = JSONObject()
        data.apply {
            put("type", "CHAT")
            put("content", content)
            put("sender", sender)
            put("planner_id", planner_id)
            put("date", null)
            put("plan_id", null)
            put("isExistComments", null)
        }
        stompClient.send("/app/chat.sendMessage", data.toString()).subscribe()
        Log.i("[WebSocket]", "<Planner update> + Planner_id : " + planner_id)
    }

    fun fetchRemoteDB(planner_id : Long){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res1 = plannerRepository.fetchPlanner(planner_id)
            when(res1){
                is BaseResult.Success -> {
                    val res2 = plannerRepository.fetchPlan(planner_id)
                    when(res2){
                        is BaseResult.Success -> {
                            _state.value = PlannerState.Success(res2.data)
                        }
                        is BaseResult.Error -> _state.value = PlannerState.Failure(res2.err.code)
                    }
                }
                is BaseResult.Error -> _state.value = PlannerState.Failure(res1.err.code)
            }
            delay(500)
            hideLoading()
        }
    }
}

sealed class PlannerState {
    object Init : PlannerState()
    data class IsLoading(val isLoading: Boolean) : PlannerState()
    data class Success(val data: Any) : PlannerState()
    data class Failure(val code: Int) : PlannerState()
}

/**
syncRemoteDB & observeTimeStamp => Polling 방식을 Websocket으로 대체

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

fun observeTimeStamp(planner_id: Long) {
runBlocking {
job.join()
}
viewModelScope.launch {
plannerRepository.latestTimeStamp(planner_id).collectLatest {
when {
//it == "NotExist" -> _state.value = PlannerState.Failure(500)
it == "NoInternet" -> _state.value = PlannerState.Failure(0)
lastTimeStamp != it -> setUpdate()
else -> setNotUpdate()
}
}
}
}
 **/