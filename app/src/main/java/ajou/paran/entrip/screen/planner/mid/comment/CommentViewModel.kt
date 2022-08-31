package ajou.paran.entrip.screen.planner.mid.comment

import ajou.paran.entrip.repository.Impl.CommentRepositoryImpl
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
import org.json.JSONObject
import ua.naiksoftware.stomp.StompClient
import javax.inject.Inject

@HiltViewModel
class CommentViewModel
@Inject
constructor(
    private val commentRepository: CommentRepositoryImpl,
    private val stompClient: StompClient
): ViewModel() {
    private val _state = MutableStateFlow<ApiState>(ApiState.Init)
    val state : StateFlow<ApiState> get() = _state

    fun setLoading(){
        _state.value = ApiState.IsLoading(true)
    }

    fun hideLoading(){
        _state.value = ApiState.IsLoading(false)
    }

    fun insertComment(user_id : String, content : String, plan_id : Long){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = commentRepository.insertComment(user_id, content, plan_id)
            when(res){
                is BaseResult.Success -> {
                    _state.value = ApiState.Success(res.data)
                    sendCommentChangeMessage(user_id, 2, plan_id, true)
                }
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun deleteComment(user_id : String, comment_id : Long, plan_id: Long){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = commentRepository.deleteComment(comment_id, plan_id)
            when(res){
                is BaseResult.Success -> {
                    _state.value = ApiState.Success(res.data)
                    sendCommentChangeMessage(user_id, 2, plan_id, !res.data.isNullOrEmpty())
                }
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun selectComment(plan_id : Long){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = commentRepository.selectComment(plan_id)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(res.data)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun sendCommentChangeMessage(sender : String, content : Int, plan_id : Long, isExistComments : Boolean){
        val data = JSONObject()
        data.apply {
            put("type", "CHAT")
            put("content", content)
            put("sender", sender)
            put("planner_id", null)
            put("date", null)
            put("plan_id", plan_id)
            put("isExistComments", isExistComments)
        }
        stompClient.send("/app/chat.sendMessage", data.toString()).subscribe()

        Log.e("[WebSocket]", "<Comment update>  + Plan_id : "+plan_id)
    }
}