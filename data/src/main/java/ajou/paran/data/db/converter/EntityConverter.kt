package ajou.paran.data.db.converter

import ajou.paran.data.db.entity.PlanEntity
import ajou.paran.data.db.entity.PlannerEntity
import ajou.paran.data.remote.model.response.FindAllPlannersResponse
import ajou.paran.data.remote.model.response.FindAllPlannersResponseList
import ajou.paran.domain.model.BasePlan
import ajou.paran.domain.model.BasePlanner

internal fun BasePlan.toEntity() = PlanEntity(
    planId = planId,
    plannerIdFK = plannerId,
    todo = todo,
    time = time,
    location = location,
    date = date,
    rgb = rgb,
    isExistComments = isExistComments
)

internal fun PlanEntity.toModel() = BasePlan(
    planId = planId,
    plannerId = plannerIdFK,
    todo = todo,
    time = time,
    location = location,
    date = date,
    rgb = rgb,
    isExistComments = isExistComments
)

internal fun BasePlanner.toEntity() = PlannerEntity(
    plannerId = id,
    title = title,
    startDate = startDate,
    endDate = endDate,
    timeStamp = timeStamp,
    commentTimeStamp = commentTimeStamp
)

internal fun PlannerEntity.toModel() = BasePlanner(
    id = plannerId,
    title = title,
    startDate = startDate,
    endDate = endDate,
    timeStamp = timeStamp,
    commentTimeStamp = commentTimeStamp
)

internal fun FindAllPlannersResponse.toEntity() = PlannerEntity(
    plannerId = plannerId,
    title = title,
    startDate = startDate,
    endDate = endDate,
    timeStamp = timeStamp,
    commentTimeStamp = commentTimeStamp
)

internal fun FindAllPlannersResponseList.toEntity() = this.map { it.toEntity() }
