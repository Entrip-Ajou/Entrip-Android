package ajou.paran.entrip.screen.planner.mid.input

import ajou.paran.entrip.R
import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.repository.room.plan.repository.PlanRepository
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputViewModel @Inject constructor(
    private val planRepository: PlanRepository
    ): ViewModel() {
    companion object{
        private const val TAG = "[InputViewModel]"
    }

    var isUpdate : Boolean = false
    var update_id : Long = 0L

    lateinit var date : String
    lateinit var title : String
    lateinit var planner_id : String


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

    private var _inputState = MutableLiveData<InputState>()
    val inputState : LiveData<InputState>
        get() = _inputState

    // 필수 기입 항목 확인
    fun checkInput(){
        when{
            todo.value.isNullOrEmpty() && time.value.isNullOrEmpty() -> {
                _inputState.value = InputState.initialized
            }
            todo.value.isNullOrEmpty() -> {
                _inputState.value = InputState.NoTodo
            }
            time.value.isNullOrEmpty() -> {
                _inputState.value = InputState.NoTime
            }
            !todo.value.isNullOrEmpty() && !time.value.isNullOrEmpty() -> {
                _inputState.value = InputState.Success

                // time이 String 형태로 ex) 16 : 20 문자열 그대로 찍히기 때문에, Room에 데이터를 넣을 때는 int 값으로 변환하여 넣는다.
                val timeArray = time.value.toString().split(":")
                val timeToString = timeArray[0].plus(timeArray[1])
                val timeToInt = Integer.parseInt(timeToString)

                // 아무것도 선택을 안할 시, -1이 rgb에 할당되므로 이를 렌더링 할 수가 없다.
                if(rgb.value!! < 0) rgb.value = R.color.white

                if(!isUpdate){
                    viewModelScope.launch(Dispatchers.IO) {
                        planRepository.insertPlan(
                            PlanEntity(
                                todo = todo.value.toString(),
                                rgb = rgb.value!!,
                                time = timeToInt,
                                location = location.value,
                                date = date,
                                title = title,
                                planner_id = planner_id
                            )
                        )
                    }
                }else{
                    viewModelScope.launch(Dispatchers.IO){
                        planRepository.updatePlan(
                                todo.value.toString(),
                                rgb.value!!,
                                timeToInt,
                                location.value.toString(),
                                update_id
                        )
                    }
                }
            }
        }
    }

}