package ajou.paran.entrip.screen.planner.top.notice

import ajou.paran.entrip.repository.Impl.NoticeRepositoryImpl
import ajou.paran.entrip.repository.network.dto.NoticesSaveRequest
import ajou.paran.entrip.repository.network.dto.NoticesUpdateRequest
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
class NoticeViewModel
@Inject
constructor(
    private val noticeRepository: NoticeRepositoryImpl
) : ViewModel(){
    private val _state = MutableStateFlow<ApiState>(ApiState.Init)
    val state : StateFlow<ApiState> get() = _state

    fun setLoading() {
        _state.value = ApiState.IsLoading(true)
    }

    fun hideLoading() {
        _state.value = ApiState.IsLoading(false)
    }

    fun deleteNotice(notice_id : Long){
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = noticeRepository.deleteNotice(notice_id)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(Unit)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun fetchAllNotices(planner_id : Long){
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = noticeRepository.fetchAllNotices(planner_id)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(res.data)

                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }
}