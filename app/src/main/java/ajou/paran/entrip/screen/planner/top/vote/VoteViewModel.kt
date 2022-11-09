package ajou.paran.entrip.screen.planner.top.vote

import ajou.paran.entrip.repository.Impl.VoteRepositoryImpl
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
class VoteViewModel
@Inject
constructor(
  private val voteRepository: VoteRepositoryImpl
) : ViewModel() {
    private val _state = MutableStateFlow<ApiState>(ApiState.Init)
    val state : StateFlow<ApiState> get() = _state

    fun setLoading() {
        _state.value = ApiState.IsLoading(true)
    }

    fun hideLoading() {
        _state.value = ApiState.IsLoading(false)
    }

    fun fetchVotes(planner_id : Long) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = voteRepository.fetchVotes(planner_id)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(res.data)

                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

    fun deleteVotes(vote_id : Long){
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = voteRepository.deleteVote(vote_id)
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(Unit)

                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }

}