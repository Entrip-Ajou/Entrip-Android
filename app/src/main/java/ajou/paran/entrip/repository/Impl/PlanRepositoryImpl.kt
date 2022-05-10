package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.PlanRemoteSource
import ajou.paran.entrip.repository.network.dto.PlanRequest
import ajou.paran.entrip.repository.network.dto.PlanUpdateRequest
import ajou.paran.entrip.repository.room.plan.dao.PlanDao
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(
    private val planRemoteSource: PlanRemoteSource,
    private val planDao: PlanDao
) : PlanRepository {
    override suspend fun insertPlan(planRequest: PlanRequest) : BaseResult<Unit, Failure>{
        val plan = planRemoteSource.insertPlan(planRequest)
        if(plan is BaseResult.Success){
            planDao.insertPlan(plan.data)
            val planner = planRemoteSource.fetchPlanner(plan.data.planner_idFK)
            if(planner is BaseResult.Success){
                planDao.updatePlanner(planner.data)
                return BaseResult.Success(Unit)
            }else{
                return BaseResult.Error(Failure((planner as BaseResult.Error).err.code,planner.err.message))
            }
        }else{
            return BaseResult.Error(Failure((plan as BaseResult.Error).err.code,plan.err.message))
        }
    }

    override suspend fun deletePlan(plan_id: Long, planner_id : Long) : BaseResult<Unit, Failure> {
        val plan = planRemoteSource.deletePlan(plan_id)
        if(plan is BaseResult.Success){
            planDao.deletePlan(plan.data)
            val planner = planRemoteSource.fetchPlanner(planner_id)
            if(planner is BaseResult.Success){
                planDao.updatePlanner(planner.data)
                return BaseResult.Success(Unit)
            }else{
                return BaseResult.Error(Failure((planner as BaseResult.Error).err.code,planner.err.message))
            }
        }else{
            return BaseResult.Error(Failure((plan as BaseResult.Error).err.code, plan.err.message))
        }
    }

    override suspend fun updatePlan(plan_id: Long, plan : PlanUpdateRequest) : BaseResult<Unit, Failure> {
        val plan = planRemoteSource.updatePlan(plan_id, plan)
        if(plan is BaseResult.Success){
            planDao.updatePlan(plan.data)
            val planner = planRemoteSource.fetchPlanner(plan.data.planner_idFK)
            if(planner is BaseResult.Success){
                planDao.updatePlanner(planner.data)
                return BaseResult.Success(Unit)
            }else{
                return BaseResult.Error(Failure((planner as BaseResult.Error).err.code,planner.err.message))
            }
        }else{
            return BaseResult.Error(Failure((plan as BaseResult.Error).err.code,plan.err.message))
        }
    }

    override fun selectPlan(planDate: String, plannerId: Long): Flow<List<PlanEntity>> {
        return planDao.selectPlan(planDate, plannerId)
    }
}