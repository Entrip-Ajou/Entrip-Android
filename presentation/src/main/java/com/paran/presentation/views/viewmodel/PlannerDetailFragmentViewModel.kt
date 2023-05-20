package com.paran.presentation.views.viewmodel

import ajou.paran.domain.model.BasePlan
import ajou.paran.domain.model.BasePlanner
import ajou.paran.domain.model.PlannerDate
import ajou.paran.domain.usecase.SelectPlanByIdWithDateUseCase
import ajou.paran.domain.usecase.UpdateRemotePlannerUseCase
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
class PlannerDetailFragmentViewModel
@Inject
constructor(
    private val selectPlanByIdWithDateUseCase: SelectPlanByIdWithDateUseCase,
    private val updateRemotePlannerUseCase: UpdateRemotePlannerUseCase
) : ViewModel() {
    companion object {
        private const val TAG = "PlannerDetailFragVM"
    }

    private val _selectedPlanner = MutableLiveData<BasePlanner>()
    val selectedPlanner: LiveData<BasePlanner>
        get() = _selectedPlanner

    private val _selectedDate = MutableLiveData("")
    val selectedDate: LiveData<String>
        get() = _selectedDate

    private val _loadPlanList = MutableLiveData<List<BasePlan>>(emptyList())
    val loadPlanList: LiveData<List<BasePlan>>
        get() = _loadPlanList

    fun postPlanner(planner: BasePlanner) {
        _selectedPlanner.value = planner
        _selectedDate.value = planner.startDate
    }

    fun loadPlanData(planDate: String) = _selectedPlanner.value?.let {
        CoroutineScope(Dispatchers.IO).launch {
            selectPlanByIdWithDateUseCase(
                params = SelectPlanByIdWithDateUseCase.Params(
                    planDate = planDate,
                    plannerId = it.id
                )
            ).onSuccess {
                Log.d(TAG, "Load Plan Success")
                _loadPlanList.postValue(it)
            }.onFailure {
                Log.d(TAG, "Load Plan Failure")
                _loadPlanList.postValue(emptyList())
            }
        }
    }

    fun modifyDate(
        startDate: String,
        endDate: String
    ) = _selectedPlanner.value?.let { planner ->
        CoroutineScope(Dispatchers.IO).launch {
            updateRemotePlannerUseCase(
                params = UpdateRemotePlannerUseCase.Params(
                    plannerId = planner.id,
                    title =  planner.title,
                    startDate = startDate,
                    endDate = endDate
                )
            ).onSuccess {
                _selectedPlanner.postValue(it)
                _selectedDate.postValue(it.startDate)
            }
        }
    }

}