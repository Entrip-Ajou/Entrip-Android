package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.PlannerRemoteSource
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
): PlannerRepository {

    /**
     * @name : updatePlanner
     * @param : plannerId - Long, plannerEntity - PlannerEntity
     * @return : Success - BaseResult.Success, Fail - BaseResult.Error
     * **/
    override suspend fun updatePlanner(plannerId: Long, plannerEntity: PlannerEntity): BaseResult<Int, Failure> {
        val response = plannerRemoteSource.isExist(plannerId)
        return if (response is BaseResult.Success){
            val planner = plannerRemoteSource.updatePlanner(plannerId, plannerEntity)
            if(planner is BaseResult.Success){
                planDao.updatePlanner(plannerEntity)
                val planner = plannerRemoteSource.findPlanner(planner.data.planner_id)
                if(planner is BaseResult.Success){
                    planDao.updatePlanner(planner.data)
                    BaseResult.Success(200)
                }else{
                    BaseResult.Error(Failure(408, "Request TimeOut"))
                }
            } else {
                BaseResult.Error(Failure(408, "Request TimeOut"))
            }
        } else {
            BaseResult.Error(Failure(408, "Request TimeOut"))
        }
    }

    override suspend fun insertPlanner(plannerEntity: PlannerEntity): BaseResult<Int, Failure> {
        val planner = plannerRemoteSource.findPlanner(plannerEntity.planner_id)
        return if (planner is BaseResult.Success){
            planDao.insertPlanner(plannerEntity)
            Log.d("TAG", "Success")
            BaseResult.Success(200)
        }else{
            Log.d("TAG", "Fail")
            BaseResult.Error(Failure(408, "Request TimeOut"))
        }
    }

    override suspend fun selectAllPlan(plannerId: Long): List<PlanEntity>
            = planDao.selectAllPlan(plannerId)

    override fun deleteAllPlan(plannerId: Long) {
        planDao.deleteAllPlan(plannerId)
    }

    override fun getFlowPlanner(plannerId: Long): Flow<PlannerEntity>
            = planDao.findFlowPlanner(planner_Id = plannerId)

    override fun findDBPlanner(plannerId: Long): PlannerEntity?
        = planDao.findPlanner(plannerId)


    // --------------------------------------------------------------------

    override suspend fun selectAllPlanner() : Flow<List<PlannerEntity>>
            = planDao.selectAllPlanner()

    override suspend fun createPlanner(user_id: String) : BaseResult<PlannerEntity, Failure> {
        val remotePlanner = plannerRemoteSource.createPlanner(user_id)
        return if (remotePlanner is BaseResult.Success) {
            planDao.insertPlanner(remotePlanner.data)
            return BaseResult.Success(remotePlanner.data)
        } else {
            return BaseResult.Error(Failure(408, "Request Timeout"))
        }
    }

    override suspend fun deletePlanner(plannerId: Long): BaseResult<Unit, Failure> {
        val deletePlanner = plannerRemoteSource.deletePlanner(plannerId)
        return if (deletePlanner is BaseResult.Success) {
            planDao.deletePlanner(plannerId)
            return BaseResult.Success(Unit)
        } else {
            return BaseResult.Error(Failure(408, "Request Timeout"))
        }
    }

    override suspend fun findPlanner(plannerId: Long): BaseResult<PlannerEntity, Failure> {
        val plannerExist = plannerRemoteSource.isExist(plannerId)
        return if(plannerExist is BaseResult.Success) {
            if(plannerExist.data){
                val planner = plannerRemoteSource.findPlanner(plannerId)
                if(planner is BaseResult.Success){
                    return BaseResult.Success(planner.data)
                }else{
                    return BaseResult.Error(Failure(408, "Request Timeout"))
                }
            }else{
                // 존재하지 않는다 -> err code가 1일 경우 Message를 띄워 사용자에게 알려줘야 한다.
                planDao.deletePlanner(plannerId)
                return BaseResult.Error(Failure(1, "Already deleted"))
            }
        }else{
            return BaseResult.Error(Failure(408, "Request Timeout"))
        }
    }
}