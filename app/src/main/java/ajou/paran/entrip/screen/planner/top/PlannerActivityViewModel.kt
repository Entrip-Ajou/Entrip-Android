package ajou.paran.entrip.screen.planner.top

import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.Impl.PlannerRepositoryImpl
import ajou.paran.entrip.repository.network.dto.PlannerUpdateRequest
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.json.JSONObject
import javax.inject.Inject

/**
 * @ClassName: PlannerActivityViewModel
 * @innerFunc: setPlannerDateItem(List<PlannerDate>), getPlannerDateItem()
 * @private: _plannerDateItemList: MutableLiveData<List<PlannerDate>>
 * **/
@HiltViewModel
class PlannerActivityViewModel
@Inject
constructor(
    private val plannerRepository: PlannerRepositoryImpl
) : ViewModel() {
    companion object{
        private const val TAG = "[PlannerActViewModel]"
    }

    private val _state = MutableStateFlow<PlannerState>(PlannerState.Init)
    val state : StateFlow<PlannerState> get() = _state

    private lateinit var lastTimeStamp: String
    private lateinit var job: Job

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

    fun createPlanner(userId : String){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = plannerRepository.createPlanner(userId)
            when(res){
                is BaseResult.Success -> _state.value = PlannerState.Success(res.data)
                is BaseResult.Error -> _state.value = PlannerState.Failure(res.err.code)
            }
            delay(500)
            hideLoading()
        }
    }
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
                    it == "NotExist" -> _state.value = PlannerState.Failure(500)
                    it == "NoInternet" -> _state.value = PlannerState.Failure(0)
                    lastTimeStamp != it -> setUpdate()
                    else -> setNotUpdate()
                }
            }
        }
    }

}

sealed class PlannerState {
    object Init : PlannerState()
    data class IsLoading(val isLoading: Boolean) : PlannerState()
    data class IsUpdate(val isUpdate: Boolean) : PlannerState()
    data class Success(val data : Any) : PlannerState()
    data class Failure(val code : Int) : PlannerState()
}