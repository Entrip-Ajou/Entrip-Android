package com.paran.presentation.utils.state

import ajou.paran.domain.model.BasePlanner

sealed class PlannerState {
    object Init : PlannerState()
    data class Store(
        val data: BasePlanner
    ) : PlannerState()
}