package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.PlannerRemoteSource
import ajou.paran.entrip.repository.network.dto.PlannerUpdateRequest
import ajou.paran.entrip.repository.room.plan.dao.PlanDao
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import android.util.Log
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

class PlannerRepositoryImpl
@Inject
constructor(
    private val plannerRemoteSource: PlannerRemoteSource,
    private val planDao: PlanDao
) : PlannerRepository {

    override suspend fun updatePlanner(
        plannerId: Long,
        plannerUpdateRequest: PlannerUpdateRequest
    ): BaseResult<Unit, Failure> {
        val planner = plannerRemoteSource.updatePlanner(plannerId, plannerUpdateRequest)

        if(planner is BaseResult.Success){
            planDao.updatePlanner(planner.data)
            return BaseResult.Success(Unit)
        }else{
            return BaseResult.Error(Failure((planner as BaseResult.Error).err.code, planner.err.message))
        }
    }

    override fun getFlowPlanner(plannerId: Long): Flow<PlannerEntity> =
        planDao.findFlowPlanner(planner_Id = plannerId)


    override suspend fun selectAllPlanner(): Flow<List<PlannerEntity>> = planDao.selectAllPlanner()

    override suspend fun createPlanner(user_id: String): BaseResult<PlannerEntity, Failure> {
        val remotePlanner = plannerRemoteSource.createPlanner(user_id)
        return if (remotePlanner is BaseResult.Success) {
            planDao.insertPlanner(remotePlanner.data)
            return BaseResult.Success(remotePlanner.data)
        } else {
            return BaseResult.Error(Failure((remotePlanner as BaseResult.Error).err.code, remotePlanner.err.message))
        }
    }

    override suspend fun deletePlanner(plannerId: Long): BaseResult<Unit, Failure> {
        val deletePlanner = plannerRemoteSource.deletePlanner(plannerId)
        return if (deletePlanner is BaseResult.Success) {
            planDao.deletePlanner(plannerId)
            return BaseResult.Success(Unit)
        } else {
            return BaseResult.Error(Failure((deletePlanner as BaseResult.Error).err.code, deletePlanner.err.message))
        }
    }

    /**
     *  findPlanner 설명서 : HomeActivity에서는 local DB에 저장된 플래너들만 가져와 화면을 구성하기 때문에 isExist를 통해
     *  서버에도 해당 planner_id가 존재하는지 확인해야 하므로 1차적으로 확인(true -> 존재 / false -> 존재 x) 후
     *  존재할 때 서버 DB에서 planner_id에 해당하는 Planner 객체를 가져와야 한다.
     *
     *  예외 1) isExist = false : 서버에 planner가 존재하지 않으므로 Local DB에 planner 삭제
     *         -> Flow를 통해 동적으로 Planner를 가져오므로 화면 변경 가능
     *
     *  예외 2) isExist = true -> findPlanner를 호출하는 과정에서 다른 사용자로 인해 삭제된 경우
     *         -> err.code = 500 으로 return (서버에서 IllegalArgumentException로 처리)
     *
     *  예외 3) isExist = true -> findPlanner를 호출하는 과정에서 네트워크 지연으로 인한 request 실패
     *         -> err.code = 0 으로 return (클라이언트에서 NoInternetException로 처리)
     */

    override suspend fun findPlanner(plannerId: Long): BaseResult<PlannerEntity, Failure> {
        val plannerExist = plannerRemoteSource.isExist(plannerId)
        return if (plannerExist is BaseResult.Success) {
            if (plannerExist.data) {
                val planner = plannerRemoteSource.findPlanner(plannerId)
                if (planner is BaseResult.Success) {
                    return BaseResult.Success(planner.data)
                } else {
                    return BaseResult.Error(Failure((planner as BaseResult.Error).err.code, planner.err.message))
                }
            } else {
                // 존재하지 않는다 -> err code가 1일 경우 Message를 띄워 사용자에게 알려줘야 한다.
                planDao.deletePlanner(plannerId)
                return BaseResult.Error(Failure(500, "Already deleted"))
            }
        } else {
            return BaseResult.Error(Failure((plannerExist as BaseResult.Error).err.code, plannerExist.err.message))
        }
    }
}