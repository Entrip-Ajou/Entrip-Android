package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.InviteEntity
import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.PlannerRemoteSource
import ajou.paran.entrip.repository.network.dto.PlannerUpdateRequest
import ajou.paran.entrip.repository.room.plan.dao.PlanDao
import ajou.paran.entrip.repository.room.plan.dao.UserDao
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

class PlannerRepositoryImpl
@Inject
constructor(
    private val plannerRemoteSource: PlannerRemoteSource,
    private val planDao: PlanDao,
    private val userDao: UserDao
) : PlannerRepository {

    override suspend fun updatePlanner(
        plannerId: Long,
        plannerUpdateRequest: PlannerUpdateRequest
    ): BaseResult<Unit, Failure> {
        val planner = plannerRemoteSource.updatePlanner(plannerId, plannerUpdateRequest)

        if (planner is BaseResult.Success) {
            planDao.updatePlanner(planner.data)
            return BaseResult.Success(Unit)
        } else {
            return BaseResult.Error(
                Failure(
                    (planner as BaseResult.Error).err.code,
                    planner.err.message
                )
            )
        }
    }

    override fun getFlowPlanner(plannerId: Long): Flow<PlannerEntity> =
        planDao.findFlowPlanner(planner_Id = plannerId)

    fun deleteInvitation(inviteEntity: InviteEntity) = userDao.deleteInvite(inviteEntity)

    override suspend fun selectAllPlanner(): Flow<List<PlannerEntity>> = planDao.selectAllPlanner()

    override suspend fun createPlanner(userId: String): BaseResult<PlannerEntity, Failure> {
        val remotePlanner = plannerRemoteSource.createPlanner(userId)
        return if (remotePlanner is BaseResult.Success) {
            planDao.insertPlanner(remotePlanner.data)
            return BaseResult.Success(remotePlanner.data)
        } else {
            return BaseResult.Error(
                Failure(
                    (remotePlanner as BaseResult.Error).err.code,
                    remotePlanner.err.message
                )
            )
        }
    }

    override suspend fun deletePlanner(
        user_id: String,
        plannerId: Long
    ): BaseResult<Boolean, Failure> {
        val deletePlanner = plannerRemoteSource.deletePlanner(user_id, plannerId)
        return if (deletePlanner is BaseResult.Success) {
            planDao.deletePlanner(plannerId)
            userDao.deleteWaitWithPlannerId(plannerId)
            BaseResult.Success(deletePlanner.data)
        } else {
            BaseResult.Error(
                Failure(
                    (deletePlanner as BaseResult.Error).err.code,
                    deletePlanner.err.message
                )
            )
        }
    }

    override suspend fun fetchPlanner(
        planner_id: Long
    ): BaseResult<Unit, Failure> {
        val remotePlanner = plannerRemoteSource.fetchPlanner(planner_id)
        if (remotePlanner is BaseResult.Success) {
            planDao.updatePlanner(remotePlanner.data)
            return BaseResult.Success(Unit)
        } else {
            return BaseResult.Error(
                Failure(
                    (remotePlanner as BaseResult.Error).err.code,
                    remotePlanner.err.message
                )
            )
        }
    }

    override suspend fun fetchPlan(
        planner_id : Long
    ) : BaseResult<Unit, Failure> {
        val res = plannerRemoteSource.fetchPlans(planner_id)
        when(res){
            is BaseResult.Success ->{
                savePlanToLocal(res.data, planner_id)
                return BaseResult.Success(Unit)
            }
            is BaseResult.Error -> {
                return BaseResult.Error(Failure(res.err.code, res.err.message))
            }
        }
    }

    suspend fun acceptInvitation(planner_id: Long): BaseResult<Unit, Failure> {
        val planner = plannerRemoteSource.fetchPlanner(planner_id)
        if (planner is BaseResult.Success) {
            planDao.insertPlanner(planner.data)
            val plans = plannerRemoteSource.fetchPlans(planner_id)
            if (plans is BaseResult.Success) {
                planDao.insertAllPlan(plans.data)
                return BaseResult.Success(Unit)
            } else {
                return BaseResult.Error(
                    Failure(
                        (plans as BaseResult.Error).err.code,
                        plans.err.message
                    )
                )
            }
        } else {
            return BaseResult.Error(
                Failure(
                    (planner as BaseResult.Error).err.code,
                    planner.err.message
                )
            )
        }
    }

    override suspend fun isExist(planner_id: Long): BaseResult<Boolean, Failure> {
        val res = plannerRemoteSource.isExist(planner_id)
        if (res is BaseResult.Success) return BaseResult.Success(res.data)
        else return BaseResult.Error(Failure((res as BaseResult.Error).err.code, res.err.message))
    }

    suspend fun findByPlannerIdWithDate(planner_id: Long, date: String) : BaseResult<Unit, Failure> {
        val res = plannerRemoteSource.findByPlannerIdWithDate(planner_id, date)
        if(res is BaseResult.Success){
            val dateFormat = date.substring(0,4) + "/" + date.substring(4,6) + "/" + date.substring(6,8)
            planDao.deletePlanWithDate(planner_id, dateFormat)
            planDao.insertAllPlan(res.data)
            return BaseResult.Success(Unit)
        }
        else return BaseResult.Error(Failure((res as BaseResult.Error).err.code, res.err.message))
    }

    private fun savePlanToLocal(plans: List<PlanEntity>, planner_idFK: Long) {
        planDao.deleteAllPlan(planner_idFK)
        planDao.insertAllPlan(plans)
    }

    fun updateIsExistComments(isExistComments : Boolean, plan_id : Long){
        planDao.updateIsExistComments(isExistComments, plan_id)
    }
}


/*
Home 화면에서 기존의 planner를 선택했을 때 호출되는 함수 -> Server DB와 Local DB를 동기화
Logic 1) RemoteDB에서 planner fetch
2) LocalDB에서 planner fetch
3) timestamp 비교 -> 같을 때 : 최신상태 / 다를 때 : Update 필요
4) 위의 3) 결과가 같을 때 -> local planner + plan fetch
5) 위의 3) 결과가 다를 때 -> 1)에서 가져온 planner로 내부 DB update & Remote DB에서 plan 가져오기
5-1) transcation

override suspend fun syncRemoteDB(
    planner_id: Long
): Flow<BaseResult<Any, Failure>> {
    return flow {
        val remotePlanner = plannerRemoteSource.fetchPlanner(planner_id)
        val localPlanner = planDao.findPlanner(planner_id)

        if (remotePlanner is BaseResult.Success) {
            val remoteTimestamp: String = remotePlanner.data.time_stamp
            val localTimestamp: String = localPlanner!!.time_stamp

            if (remoteTimestamp.equals(localTimestamp)) {
                emit(BaseResult.Success(localTimestamp))
            } else {
                // 최신 상태 x -> remoteDB fetch
                planDao.updatePlanner(remotePlanner.data)
                val remoteDB_plan = plannerRemoteSource.fetchPlans(planner_id)
                if (remoteDB_plan is BaseResult.Success) {
                    savePlanToLocal(remoteDB_plan.data, planner_id)
                    emit(BaseResult.Success(remoteTimestamp))
                } else {
                    emit(
                        BaseResult.Error(
                            Failure(
                                (remoteDB_plan as BaseResult.Error).err.code,
                                remoteDB_plan.err.message
                            )
                        )
                    )
                }
            }
        } else {
            emit(
                BaseResult.Error(
                    Failure(
                        (remotePlanner as BaseResult.Error).err.code,
                        remotePlanner.err.message
                    )
                )
            )
        }
    }
}

private fun savePlanToLocal(plans: List<PlanEntity>, planner_idFK: Long) {
    planDao.deleteAllPlan(planner_idFK)
    planDao.insertAllPlan(plans)
}

fun latestTimeStamp(planner_id: Long): Flow<String> {
    return flow {
        while (true) {
            val planner = plannerRemoteSource.fetchPlanner(planner_id)
            if (planner is BaseResult.Success) {
                val latestTimeStamp = planner.data.time_stamp
                emit(latestTimeStamp)
                delay(5000)
            } else {
                if ((planner as BaseResult.Error).err.code == 0) {
                    emit("NoInternet")
                } else {
                    emit("NotExist")
                }
                break
            }

        }
    }.flowOn(Dispatchers.IO)
}*/
