package com.paran.presentation.views.viewmodel

import ajou.paran.domain.model.BasePlanner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlannerDetailFragmentViewModel
@Inject
constructor(


) : ViewModel() {
    companion object {
        private const val TAG = "PlannerDetailFragVM"
    }

    private val _selectedPlanner = MutableLiveData<BasePlanner>()
    val selectedPlanner: LiveData<BasePlanner>
        get() = _selectedPlanner

    fun postPlanner(planner: BasePlanner) {
        _selectedPlanner.value = planner
    }

}