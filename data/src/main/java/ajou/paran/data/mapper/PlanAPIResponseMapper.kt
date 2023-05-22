package ajou.paran.data.mapper

import ajou.paran.data.remote.model.response.AddPlanResponse
import ajou.paran.domain.model.BasePlan

internal fun AddPlanResponse.toModel() = BasePlan(
    planId = planId,
    plannerId = plannerId,
    todo = todo,
    time = time,
    location = location,
    date = date,
    rgb = rgb,
    isExistComments = isExistComments
)