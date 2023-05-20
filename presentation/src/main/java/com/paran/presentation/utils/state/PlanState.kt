package com.paran.presentation.utils.state

sealed class PlanState {
    object Init : PlanState()
    data class Store(
        val plannerId: Long,
        val selectedDate: String
    ) : PlanState()
}