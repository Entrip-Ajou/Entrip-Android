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

        if(planner is BaseResult.Success){
            planDao.updatePlanner(planner.data)
            return BaseResult.Success(Unit)
        }else{
            return BaseResult.Error(Failure((planner as BaseResult.Error).err.code, planner.err.message))
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
            return BaseResult.Error(Failure((remotePlanner as BaseResult.Error).err.code, remotePlanner.err.message))
        }
    }

    override suspend fun deletePlanner(user_id : String, plannerId: Long): BaseResult<Boolean, Failure> {
        val deletePlanner = plannerRemoteSource.deletePlanner(user_id, plannerId)
        return if (deletePlanner is BaseResult.Success) {
            planDao.deletePlanner(plannerId)
            userDao.deleteWaitWithPlannerId(plannerId)
            return BaseResult.Success(deletePlanner.data)
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

    /**
     * Home 화면에서 기존의 planner를 선택했을 때 호출되는 함수 -> Server DB와 Local DB를 동기화
     * Logic 1) RemoteDB에서 planner fetch
     *       2) LocalDB에서 planner fetch
     *       3) timestamp 비교 -> 같을 때 : 최신상태 / 다를 때 : Update 필요
     *       4) 위의 3) 결과가 같을 때 -> local planner + plan fetch
     *       5) 위의 3) 결과가 다를 때 -> 1)에서 가져온 planner로 내부 DB update & Remote DB에서 plan 가져오기
     *       5-1) transcation

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
                }else{
                    // 최신 상태 x -> remoteDB fetch
                    planDao.updatePlanner(remotePlanner.data)
                    val remoteDB_plan = plannerRemoteSource.fetchPlans(planner_id)
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
                val planner = plannerRemoteSource.fetchPlanner(planner_id)
                if(planner is BaseResult.Success){
                    val latestTimeStamp = planner.data.time_stamp
                    emit(latestTimeStamp)
                    delay(5000)
                }else{
                    if((planner as BaseResult.Error).err.code == 0){
                        emit("NoInternet")
                    }else{
                        emit("NotExist")
                    }
                    break
                }

            }
        }.flowOn(Dispatchers.IO)
    }
     *
     *
    **/

    override suspend fun fetchPlanner(
        planner_id : Long
    ) : BaseResult<Unit, Failure> {
        val remotePlanner = plannerRemoteSource.fetchPlanner(planner_id)
        if (remotePlanner is BaseResult.Success) {
            planDao.updatePlanner(remotePlanner.data)
            return BaseResult.Success(Unit)
        }else{
            return BaseResult.Error(Failure((remotePlanner as BaseResult.Error).err.code, remotePlanner.err.message))
        }
    }


    suspend fun acceptInvitation(planner_id : Long) : BaseResult<Unit, Failure>{
        val planner = plannerRemoteSource.fetchPlanner(planner_id)
        if(planner is BaseResult.Success){
            planDao.insertPlanner(planner.data)
            val plans = plannerRemoteSource.fetchPlans(planner_id)
            if(plans is BaseResult.Success){
                planDao.insertAllPlan(plans.data)
                return BaseResult.Success(Unit)
            }else{
                return BaseResult.Error(Failure((plans as BaseResult.Error).err.code, plans.err.message))
            }
        }else{
            return BaseResult.Error(Failure((planner as BaseResult.Error).err.code, planner.err.message))
        }
    }

    override suspend fun isExist(planner_id : Long) : BaseResult<Boolean, Failure>{
        val res = plannerRemoteSource.isExist(planner_id)
        if(res is BaseResult.Success) return BaseResult.Success(res.data)
        else return BaseResult.Error(Failure((res as BaseResult.Error).err.code, res.err.message))
    }
}