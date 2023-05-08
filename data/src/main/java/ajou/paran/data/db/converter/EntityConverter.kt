package ajou.paran.data.db.converter

import ajou.paran.data.db.entity.PlanEntity
import ajou.paran.data.db.entity.PlannerEntity
import ajou.paran.domain.model.BasePlan
import ajou.paran.domain.model.BasePlanner

internal fun BasePlan.toEntity() = PlanEntity(
    planId = planId,
    plannerIdFK = plannerId,
    todo = todo,
    time = time,
    location = location,
    date = date,
    isExistComments = isExistComments
)

internal fun PlanEntity.toModel() = BasePlan(
    planId = planId,
    plannerId = plannerIdFK,
    todo = todo,
    time = time,
    location = location,
    date = date,
    isExistComments = isExistComments
)

internal fun BasePlanner.toEntity() = PlannerEntity(
    plannerId = id,
    title = title,
    startDate = startDate,
    endDate = endDate,
)

internal fun PlannerEntity.toModel() = BasePlanner(
    id = plannerId,
    title = title,
    startDate = startDate,
    endDate = endDate,
)

