package com.paran.presentation.views.viewmodel

import ajou.paran.domain.usecase.AddPlanUseCase
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanInputFragmentViewModel
@Inject
constructor(
    private val addPlanUseCase: AddPlanUseCase
) : ViewModel() {
    companion object {
        private const val TAG = "PlanInputFragVM"
    }

    private val _plannerId = MutableLiveData<Long>(0)
    val plannerId: LiveData<Long>
        get() = _plannerId

    private val _selectedDate = MutableLiveData("")
    val selectedDate: LiveData<String>
        get() = _selectedDate

    val inputTodo = MutableLiveData("")
    val inputTime = MutableLiveData("")
    val inputLocation = MutableLiveData("")


    fun postDate(
        plannerId: Long,
        selectedDate: String
    ) {
        _plannerId.value = plannerId
        _selectedDate.value = selectedDate
    }

    @Throws(IllegalArgumentException::class)
    fun checkInputData() {
        require(inputTodo.value?.isNotEmpty() == true)
        require(inputTime.value?.isNotEmpty() == true)
        inputTodo.value?.let { todo ->
            inputTime.value?.let {  time ->
                val convertTime = time.split(":").joinToString("").toInt()

                CoroutineScope(Dispatchers.IO).launch {
                    addPlanUseCase(
                        params = AddPlanUseCase.Params(
                            plannerId = plannerId.value!!,
                            todo = todo,
                            date = selectedDate.value!!,
                            time = convertTime,
                            location = inputLocation.value,
                            rgb = 0,
                        )
                    ).onSuccess {
                        Log.d(TAG, "Success")
                    }.onFailure {
                        Log.d(TAG, "Failure $it")
                    }
                }
            }
        }

    }

}