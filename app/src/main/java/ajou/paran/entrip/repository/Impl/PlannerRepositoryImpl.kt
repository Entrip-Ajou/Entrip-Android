package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerDate
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.PlannerRemoteSource
import ajou.paran.entrip.repository.room.plan.dao.PlanDao
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import android.util.Log
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PlannerRepositoryImpl
@Inject
constructor(
    private val plannerRemoteSource: PlannerRemoteSource,
    private val planDao: PlanDao
): PlannerRepository {

    /**
     * @name : createPlanner
     * @param : user_id - String
     * @return : Success - planner_id - Long, Fail - -1L
     * **/
    override suspend fun createPlanner(user_id: String) : Long {
        val remotePlanner = plannerRemoteSource.createPlanner(user_id)
        return if (remotePlanner is BaseResult.Success) {
            planDao.insertPlanner(remotePlanner.data)
            remotePlanner.data.planner_id
        } else {
            Log.e("[PlannerRepositoryImpl]", "Network를 확인하세요 $remotePlanner")
            -1
        }
    }

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

    /**
     * @name : findPlanner
     * @param : plannerId - Long
     * @return : Success - PlannerEntity, Fail - PlannerEntity(id = -1L)
     * **/
    override suspend fun findPlanner(plannerId: Long): PlannerEntity {
        val plannerExist = plannerRemoteSource.isExist(plannerId)
        return if(plannerExist is BaseResult.Success){
            val planner = plannerRemoteSource.findPlanner(plannerId)
            if (planner is BaseResult.Success){
                planner.data.let { t ->
                    PlannerEntity(
                        planner_id = t.planner_id,
                        title = t.title,
                        start_date = t.start_date,
                        end_date = t.end_date,
                        timeStamp = t.timeStamp
                    )
                }
            } else {
                PlannerEntity(
                    planner_id = -1L,
                    title = "제목 없음",
                    start_date = "1000/01/01",
                    end_date = "1000/12/30",
                    timeStamp = "null"
                )
            }
        }else{
            PlannerEntity(
                planner_id = -1L,
                title = "제목 없음",
                start_date = "1000/01/01",
                end_date = "1000/12/30",
                timeStamp = "null"
            )
        }
    }

    /**
     * @name : deletePlanner
     * @param : plannerId - Long
     * @return : Success - 1L, Fail - -1L
     * **/
    override suspend fun deletePlanner(plannerId: Long): Long {
        val deletePlanner = plannerRemoteSource.deletePlanner(plannerId)
        return if (deletePlanner is BaseResult.Success) {
            if (planDao.findPlanner(plannerId) != null)
                planDao.deletePlanner(plannerId)
            deletePlanner.data
        } else {
            Log.e("[PlannerRepositoryImpl]", "Network를 확인하세요 $deletePlanner")
            -1
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

}