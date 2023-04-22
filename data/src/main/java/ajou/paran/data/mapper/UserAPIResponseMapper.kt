package ajou.paran.data.mapper

import ajou.paran.data.remote.model.response.BaseResponse
import ajou.paran.domain.model.BaseCondition

internal fun BaseResponse<Boolean>.toModel() = BaseCondition(data)