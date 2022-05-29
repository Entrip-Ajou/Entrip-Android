package ajou.paran.entrip.repository.usecase

import ajou.paran.entrip.repository.Impl.UserRepository
import ajou.paran.entrip.repository.network.dto.PlannerResponse
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetUserPlannersUseCase
@Inject
constructor(
    private val userRepository: UserRepository
){
    fun execute(user_id: String): Flow<BaseResult<List<PlannerResponse>, Failure>> = userRepository.getUserPlanners(user_id).flowOn(
        Dispatchers.IO)
}