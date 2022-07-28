package ajou.paran.entrip.screen.home

import ajou.paran.entrip.model.test.nullRecommenItem
import ajou.paran.entrip.repository.Impl.PlannerRepository
import ajou.paran.entrip.repository.network.dto.TripResponse
import ajou.paran.entrip.repository.usecase.GetUserUseCase
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
class HomeFragmentViewModel
@Inject
constructor(
    private val plannerRepository: PlannerRepository,
    private val getUserUseCase: GetUserUseCase
)
: ViewModel() {
    companion object{
        const val TAG = "[HomeFragmentVM]"
    }

    private val _state = MutableStateFlow<ApiState>(ApiState.Init)
    val state : StateFlow<ApiState> get() = _state

    private val _recommendItemList: MutableLiveData<List<TripResponse>> = MutableLiveData()
    val recommendItemList: LiveData<List<TripResponse>>
        get() = _recommendItemList

    fun setLoading() {
        _state.value = ApiState.IsLoading(true)
    }

    fun hideLoading() {
        _state.value = ApiState.IsLoading(false)
    }

    suspend fun selectAllPlanner() = plannerRepository.selectAllPlanner()

    fun createPlanner(userId : String)
    = viewModelScope.launch(Dispatchers.IO){
        setLoading()
        val res = plannerRepository.createPlanner(userId)
        delay(500)
        hideLoading()
        when(res){
            is BaseResult.Success -> { _state.value = ApiState.Success(res.data) }
            is BaseResult.Error -> { _state.value = ApiState.Failure(res.err.code) }
        }
    }


    fun deletePlanner(plannerId : Long)
    = viewModelScope.launch(Dispatchers.IO){
        setLoading()
        val res = plannerRepository.deletePlanner(plannerId)
        delay(500)
        hideLoading()
        when(res){
            is BaseResult.Success -> _state.value = ApiState.Success(Unit)
            is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
        }
    }


    fun selectPlanner(plannerId : Long)
    = viewModelScope.launch(Dispatchers.IO){
        setLoading()
        val res = plannerRepository.findPlanner(plannerId)
        delay(500)
        hideLoading()
        when(res){
            is BaseResult.Success -> { _state.value = ApiState.Success(res.data) }
            is BaseResult.Error -> { _state.value = ApiState.Failure(res.err.code) }
        }
    }


    fun findByUserId(user_id: String)
    = viewModelScope.launch {
        getUserUseCase
            .execute(user_id)
            .collect{
                when(it) {
                    is BaseResult.Success -> { _recommendItemList.postValue(it.data) }
                    is BaseResult.Error -> { _recommendItemList.postValue(nullRecommenItem) }
                }
            }
    }

}