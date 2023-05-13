package com.paran.presentation.views.viewmodel

import ajou.paran.domain.model.BasePlanner
import ajou.paran.domain.usecase.SelectAllPlannerUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlannerFragmentViewModel
@Inject
constructor(
    private val selectAllPlannerUseCase: SelectAllPlannerUseCase
) : ViewModel() {
    companion object {
        private const val TAG = "PlannerFragVM"
    }

    private val _plannerList = MutableLiveData<List<BasePlanner>>(listOf())
    val plannerList: LiveData<List<BasePlanner>>
        get() = _plannerList

    fun loadAllPlanner() = CoroutineScope(Dispatchers.Default).launch {
        selectAllPlannerUseCase().onSuccess {
            _plannerList.postValue(it)
        }
    }

}