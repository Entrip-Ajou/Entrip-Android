package ajou.paran.entrip.screen.planner.top.vote.find

import ajou.paran.entrip.repository.Impl.VoteRepositoryImpl
import ajou.paran.entrip.repository.network.dto.PreviousVotesContentsRequestDto
import ajou.paran.entrip.repository.network.dto.VotesContentsCountRequestDto
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
class VoteFindViewModel
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

    fun getVotes(vote_id : Long) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = voteRepository.getVotes(vote_id)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(res.data)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }


    fun getPreviousVotes(user_id : String, vote_id : Long){
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val dto = PreviousVotesContentsRequestDto(
                user_id = user_id,
                vote_id = vote_id
            )
            val res = voteRepository.getPreviousVotes(dto)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(res.data)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun doVote(vote_id : Long, voteContents: MutableList<Long>, user_id : String){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val dto = VotesContentsCountRequestDto(
                votesId = vote_id,
                voteContentIds = voteContents,
                userId = user_id
            )
            val res = voteRepository.doVote(dto)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(res.data)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun undoVote(vote_id : Long, voteContents: MutableList<Long>, user_id : String){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val dto = VotesContentsCountRequestDto(
                votesId = vote_id,
                voteContentIds = voteContents,
                userId = user_id
            )
            val res = voteRepository.undoVotes(dto)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(Unit)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun terminate(vote_id:Long) {
        viewModelScope.launch(Dispatchers.IO){
            val res = voteRepository.terminateVote(vote_id)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(res.data)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

}