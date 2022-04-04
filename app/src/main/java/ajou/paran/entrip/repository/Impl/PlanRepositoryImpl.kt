package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.PlanRemoteSource
import ajou.paran.entrip.repository.room.plan.dao.PlanDao
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import android.util.Log

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(
    private val planRemoteSource: PlanRemoteSource,
    private val planDao: PlanDao
) : PlanRepository {
    override suspend fun insertPlan(planEntity: PlanEntity) {
        planDao.insertPlan(planEntity)
    }

    override fun selectPlan(planDate: String, plannerId: Long): List<PlanEntity> {
        return planDao.selectPlan(planDate, plannerId)
    }

    override fun deletePlan(planEntity: PlanEntity) {
        planDao.deletePlan(planEntity)
    }

    override fun updatePlan(todo: String, rgb: Int, time: Int, location: String, id: Long) {
        planDao.updatePlan(todo, rgb, time, location, id)
    }

    /**
     * Home 화면에서 planner 추가 버튼을 눌렀을 때 호출되는 함수
     */
    override suspend fun createPlanner(): Long {
        val remotePlanner = planRemoteSource.createPlanner()
        if (remotePlanner is BaseResult.Success) {
            planDao.insertPlanner(remotePlanner.data)
            return remotePlanner.data.planner_id
        } else {
            Log.e("[PlanRepositoryImpl]", "Network를 확인하세요")
            return -1
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
     */
    override suspend fun syncRemoteDB(
        plannerEntity: PlannerEntity
    ): Flow<BaseResult<Any, Failure>> {
        return flow {
            val planner_idFK = plannerEntity.planner_id

            val remotePlanner = planRemoteSource.fetchPlanner(planner_idFK)
            val localPlanner = planDao.findPlanner(planner_idFK)

            if (remotePlanner is BaseResult.Success) {
                val remoteTimestamp: LocalDateTime = remotePlanner.data.timeStamp
                val localTimestamp: LocalDateTime = localPlanner.timeStamp

                if (!remoteTimestamp.equals(localTimestamp)) {
                    // 최신 상태 x -> remoteDB fetch
                    planDao.updatePlanner(remotePlanner.data)
                    val remoteDB_plan = planRemoteSource.fetchPlans(planner_idFK)
                    if (remoteDB_plan is BaseResult.Success) {
                        savePlanToLocal(remoteDB_plan.data, planner_idFK)
                        emit(remoteDB_plan)
                    } else {
                        Log.e("[PlanRepositoryImpl]", "Network를 확인하세요")
                        /**
                         *   Todo : 추가적인 Error Handling 필요(ex. 화면에 Dialong or Toast message)
                         **/
                    }
                }
            } else {
                Log.e("[PlanRepositoryImpl]", "Network를 확인하세요")
                /**
                 *   Todo : 추가적인 Error Handling 필요(ex. 화면에 Dialong or Toast message)
                 **/
            }
        }
    }

    private fun savePlanToLocal(plans: List<PlanEntity>, planner_idFK: Long) {
        planDao.deleteAllPlan(planner_idFK)
        planDao.insertAllPlan(plans)
    }
}