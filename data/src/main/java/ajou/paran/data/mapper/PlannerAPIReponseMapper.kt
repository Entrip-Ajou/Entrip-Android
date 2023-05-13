package ajou.paran.data.mapper

import ajou.paran.data.remote.model.response.CreatePlannerByUserIdResponse
import ajou.paran.data.remote.model.response.FindPlannerByIdResponse
import ajou.paran.data.remote.model.response.UpdatePlannerResponse
import ajou.paran.domain.model.BasePlanner

internal fun CreatePlannerByUserIdResponse.toModel(): BasePlanner = BasePlanner(
    id = plannerId,
    title = title,
    startDate = startDate,
    endDate = endDate,
    timeStamp = timeStamp,
    commentTimeStamp = commentTimeStamp
)

internal fun UpdatePlannerResponse.toModel(): BasePlanner = BasePlanner(
    id = plannerId,
    title = title,
    startDate = startDate,
    endDate = endDate,
    timeStamp = timeStamp,
    commentTimeStamp = commentTimeStamp
)

internal fun FindPlannerByIdResponse.toModel(): BasePlanner = BasePlanner(
    id = plannerId,
    title = title,
    startDate = startDate,
    endDate = endDate,
    timeStamp = timeStamp,
    commentTimeStamp = commentTimeStamp
)
