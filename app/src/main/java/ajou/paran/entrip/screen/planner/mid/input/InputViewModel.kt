package ajou.paran.entrip.screen.planner.mid.input

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
                val timeArray = time.value.toString().split(" : ")
                val timeToString = timeArray[0].plus(timeArray[1])
                val timeToInt = Integer.parseInt(timeToString)

                viewModelScope.launch(Dispatchers.IO) {
                    planRepository.insertPlan(
                        PlanEntity(
                            todo = todo.value.toString(),
                            rgb = rgb.value!!,
                            time = timeToInt,
                            location = location.value
                        )
                    )
                }
            }
        }
    }

}