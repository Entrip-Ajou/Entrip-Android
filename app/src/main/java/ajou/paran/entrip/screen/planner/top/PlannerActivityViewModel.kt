package ajou.paran.entrip.screen.planner.top

import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.Impl.PlannerRepository
import ajou.paran.entrip.repository.network.dto.PlannerUpdateRequest
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
    private val plannerRepository: PlannerRepository
) : ViewModel() {
    companion object{
        private const val TAG = "[PlannerActViewModel]"
    }

    private val _state = MutableStateFlow<ApiState>(ApiState.Init)
    val state : StateFlow<ApiState> get() = _state

    fun setLoading() {
        _state.value = ApiState.IsLoading(true)
    }

    fun hideLoading() {
        _state.value = ApiState.IsLoading(false)
    }


    fun getFlowPlanner(plannerId : Long): Flow<PlannerEntity> = plannerRepository.getFlowPlanner(plannerId)

    fun createPlanner(userId : String){
        viewModelScope.launch(Dispatchers.IO){
            setLoading()
            val res = plannerRepository.createPlanner(userId)
            hideLoading()
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(res.data)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
        }
    }
    fun plannerChange(list: List<PlannerDate>, planner : PlannerEntity){
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = plannerRepository.updatePlanner(planner.planner_id, planner.let { t ->
                PlannerUpdateRequest(
                    title = t.title,
                    start_date = list.first().date,
                    end_date = list.last().date,
                )
            })
            hideLoading()
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(Unit)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
        }
    }

    fun plannerChange(title: String, planner : PlannerEntity){
        viewModelScope.launch(Dispatchers.IO) {
            setLoading()
            val res = plannerRepository.updatePlanner(planner.planner_id, planner.let { t ->
                PlannerUpdateRequest(
                    title = title,
                    start_date = t.start_date,
                    end_date = t.end_date,
                )
            })
            hideLoading()
            when(res){
                is BaseResult.Success -> _state.value = ApiState.Success(Unit)
                is BaseResult.Error -> _state.value = ApiState.Failure(res.err.code)
            }
        }
    }
}
