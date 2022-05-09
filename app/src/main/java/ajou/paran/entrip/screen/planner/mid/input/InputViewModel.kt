package ajou.paran.entrip.screen.planner.mid.input

import ajou.paran.entrip.R
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.Impl.PlanRepositoryImpl
import ajou.paran.entrip.repository.network.dto.PlanRequest
import ajou.paran.entrip.repository.network.dto.PlanUpdateRequest
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import android.graphics.Color
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
class InputViewModel @Inject constructor(
    private val planRepository: PlanRepositoryImpl
    ): ViewModel() {
    companion object{
        private const val TAG = "[InputViewModel]"
    }

    var isUpdate : Boolean = false
    var update_id : Long = 0L

    lateinit var date : String
    var planner_id : Long = -1L

    lateinit var selectedPlanner : PlannerEntity

    val todo : MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }

    val rgb : MutableLiveData<Int> by lazy{
        MutableLiveData<Int>(Color.WHITE)
    }

    val time : MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }

    val location : MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }

    private var _inputState = MutableStateFlow<InputState>(InputState.Init)
    val inputState : StateFlow<InputState> get() = _inputState

    // 필수 기입 항목 확인
    fun checkInput(){
        when{
            todo.value.isNullOrEmpty() && time.value.isNullOrEmpty() -> {
                _inputState.value = InputState.Empty
            }
            todo.value.isNullOrEmpty() -> {
                _inputState.value = InputState.NoTodo
            }
            time.value.isNullOrEmpty() -> {
                _inputState.value = InputState.NoTime
            }
            !todo.value.isNullOrEmpty() && !time.value.isNullOrEmpty() -> {
                _inputState.value = InputState.IsLoading(true)
                // time이 String 형태로 ex) 16 : 20 문자열 그대로 찍히기 때문에, Room에 데이터를 넣을 때는 int 값으로 변환하여 넣는다.
                val timeArray = time.value!!.split(":")
                val timeToString = timeArray[0].plus(timeArray[1])
                val timeToInt = Integer.parseInt(timeToString)

                // 아무것도 선택을 안할 시, -1이 rgb에 할당되므로 이를 렌더링 할 수가 없다.
                if(rgb.value!! == -1) rgb.value = R.color.white

                if(!isUpdate){
                    viewModelScope.launch(Dispatchers.IO) {
                        val planRequest = PlanRequest(
                            date = date,
                            todo = todo.value!!,
                            time = timeToInt,
                            location = location.value,
                            RGB = rgb.value!!,
                            planner_idFK = planner_id
                        )
                        val res = planRepository.insertPlan(planRequest)
                        delay(500)
                        if(res is BaseResult.Success){
                            _inputState.value = InputState.IsLoading(false)
                            _inputState.value = InputState.Success
                        }else{
                            _inputState.value = InputState.IsLoading(false)
                            _inputState.value = InputState.Failure((res as BaseResult.Error).err.code)
                        }
                    }
                }else{
                    viewModelScope.launch(Dispatchers.IO){
                        val planUpdateRequest = PlanUpdateRequest(
                            date = date,
                            todo = todo.value!!,
                            time = timeToInt,
                            location = location.value,
                            RGB = rgb.value!!
                        )
                        val res = planRepository.updatePlan(update_id, planUpdateRequest)
                        delay(500)
                        if(res is BaseResult.Success){
                            _inputState.value = InputState.IsLoading(false)
                            _inputState.value = InputState.Success
                        }else{
                            _inputState.value = InputState.IsLoading(false)
                            _inputState.value = InputState.Failure((res as BaseResult.Error).err.code)
                        }
                    }
                }
            }
        }
    }
}

sealed class InputState{

    object Init : InputState()

    // 아무것도 기입하지 않은 상태
    object Empty : InputState()

    // todo를 기입하지 않은 상태
    object NoTodo : InputState()

    // Time을 기입하지 않은 상태
    object NoTime : InputState()

    // 필수 기입 항목들을 모두 기입한 상태
    object Success : InputState()

    data class Failure(val code : Int) : InputState()

    data class IsLoading(val isLoading : Boolean) : InputState()
}