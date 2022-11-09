package ajou.paran.entrip.screen.planner.top.notice.post

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
class NoticePostViewModel
@Inject
constructor(
    private val noticeRepository : NoticeRepositoryImpl
) : ViewModel() {
    private val _state = MutableStateFlow<ApiState>(ApiState.Init)
    val state : StateFlow<ApiState> get() = _state

    fun setLoading() {
        _state.value = ApiState.IsLoading(true)
    }

    fun hideLoading() {
        _state.value = ApiState.IsLoading(false)
    }

    fun saveNotice(noticesSaveRequest: NoticesSaveRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = noticeRepository.saveNotice(noticesSaveRequest)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(Unit)

                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun updateNotice(notice_id : Long, noticesUpdateRequest: NoticesUpdateRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = noticeRepository.updateNotice(notice_id, noticesUpdateRequest)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(Unit)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }
}