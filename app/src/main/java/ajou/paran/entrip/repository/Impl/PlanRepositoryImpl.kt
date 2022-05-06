package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.PlanRemoteSource
import ajou.paran.entrip.repository.network.dto.PlanRequest
import ajou.paran.entrip.repository.network.dto.PlanUpdateRequest
import ajou.paran.entrip.repository.room.plan.dao.PlanDao
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
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

    /**
     * Home 화면에서 기존의 planner를 선택했을 때 호출되는 함수 -> Server DB와 Local DB를 동기화
     * Logic 1) RemoteDB에서 planner fetch
     *       2) LocalDB에서 planner fetch
     *       3) timestamp 비교 -> 같을 때 : 최신상태 / 다를 때 : Update 필요
     *       4) 위의 3) 결과가 같을 때 -> local planner + plan fetch
     *       5) 위의 3) 결과가 다를 때 -> 1)에서 가져온 planner로 내부 DB update & Remote DB에서 plan 가져오기
     *       5-1) transcation
     */
    override suspend fun syncRemoteDB(
        planner_id: Long
    ): Flow<BaseResult<Any, Failure>> {
        return flow {
            val remotePlanner = planRemoteSource.fetchPlanner(planner_id)
            val localPlanner = planDao.findPlanner(planner_id)

            if (remotePlanner is BaseResult.Success) {
                val remoteTimestamp: String = remotePlanner.data.time_stamp
                val localTimestamp: String = localPlanner!!.time_stamp

                if (remoteTimestamp.equals(localTimestamp)) {
                    emit(BaseResult.Success(localTimestamp))
                }else{
                    // 최신 상태 x -> remoteDB fetch
                    planDao.updatePlanner(remotePlanner.data)
                    val remoteDB_plan = planRemoteSource.fetchPlans(planner_id)
                    if (remoteDB_plan is BaseResult.Success) {
                        savePlanToLocal(remoteDB_plan.data, planner_id)
                        emit(BaseResult.Success(remoteTimestamp))
                    }else{
                        emit(BaseResult.Error(Failure((remoteDB_plan as BaseResult.Error).err.code, remoteDB_plan.err.message)))
                    }
                }
            }else{
                emit(BaseResult.Error(Failure((remotePlanner as BaseResult.Error).err.code, remotePlanner.err.message)))
            }
        }
    }

    private fun savePlanToLocal(plans: List<PlanEntity>, planner_idFK: Long) {
        planDao.deleteAllPlan(planner_idFK)
        planDao.insertAllPlan(plans)
    }

    fun latestTimeStamp(planner_id: Long) : Flow<String>{
        return flow{
            while(true){
                val planner = planRemoteSource.fetchPlanner(planner_id)
                if(planner is BaseResult.Success){
                    val latestTimeStamp = planner.data.time_stamp
                    emit(latestTimeStamp)
                    delay(5000)
                }else{

                    break
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}