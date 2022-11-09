package ajou.paran.entrip.screen.planner.top.vote.post

import ajou.paran.entrip.repository.Impl.VoteRepositoryImpl
import ajou.paran.entrip.repository.network.dto.NoticesSaveRequest
import ajou.paran.entrip.repository.network.dto.VotesSaveRequestDto
import ajou.paran.entrip.repository.network.dto.VotesUpdateRequestDto
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
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
class VotePostViewModel
@Inject
constructor(
    private val voteRepository : VoteRepositoryImpl
) : ViewModel() {
    private val _state = MutableStateFlow<ApiState>(ApiState.Init)
    val state : StateFlow<ApiState> get() = _state

    fun setLoading() {
        _state.value = ApiState.IsLoading(true)
    }

    fun hideLoading() {
        _state.value = ApiState.IsLoading(false)
    }

    fun saveVote(voteSaveRequest: VotesSaveRequestDto){
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = voteRepository.saveVote(voteSaveRequest)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(Unit)

                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun updateVote(votesUpdateRequestDto: VotesUpdateRequestDto){
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = voteRepository.updateVote(votesUpdateRequestDto)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(Unit)

                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }
}