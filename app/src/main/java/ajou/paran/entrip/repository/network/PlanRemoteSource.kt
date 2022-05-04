package ajou.paran.entrip.repository.network

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.api.PlanApi
import ajou.paran.entrip.repository.network.dto.PlanRequest
import ajou.paran.entrip.repository.network.dto.PlanUpdateRequest
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException


class PlanRemoteSource constructor(private val planApi: PlanApi) {

    suspend fun fetchPlans(planner_idFK: Long): BaseResult<List<PlanEntity>, Failure> {
        try {
            val response = planApi.fetchPlans(planner_idFK)
            return if (response.status == 200) {
                val plans = mutableListOf<PlanEntity>()
                response.data?.forEach { t ->
                    plans.add(
                        PlanEntity(t.id, t.planner_idFK, t.todo, t.rgb, t.time, t.location, t.date)
                    )
                }
                BaseResult.Success(plans)
            } else {
                BaseResult.Error(Failure(response.status, response.message))
            }
        } catch (e: NoInternetException) {
            return BaseResult.Error(Failure(0, e.message))
        } catch (e: Exception) {
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

    // PlannerRepositoryImpl의 findPlanner와 동일 -> 필요없으면 삭제
    suspend fun fetchPlanner(planner_id: Long): BaseResult<PlannerEntity, Failure> {
        try {
            val response = planApi.fetchPlanner(planner_id)
            return if (response.status == 200) {
                val planner = response.data?.let { t ->
                    PlannerEntity(t.planner_id, t.title, t.start_date, t.end_date, t.timeStamp)
                }
                BaseResult.Success(planner!!)
            } else {
                BaseResult.Error(Failure(response.status, response.message))
            }
        } catch (e: NoInternetException) {
            return BaseResult.Error(Failure(0, e.message))
        } catch (e: Exception) {
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

    suspend fun insertPlan(plan: PlanRequest): BaseResult<PlanEntity, Failure> {
        try {
            val response = planApi.insertPlan(plan)
            return if (response.status == 200) {
                val plan = response.data?.let { t ->
                    PlanEntity(t.id, t.planner_idFK, t.todo, t.rgb, t.time, t.location, t.date)
                }
                BaseResult.Success(plan!!)
            }else{
                BaseResult.Error(Failure(response.status, response.message))
            }
        }catch(e: NoInternetException){
            return BaseResult.Error(Failure(0, e.message))
        }catch(e: Exception){
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

    suspend fun deletePlan(plan_id: Long): BaseResult<Long, Failure>{
        try{
            val response = planApi.deletePlan(plan_id)
            return if(response.status == 200){
                BaseResult.Success(response.data!!)
            }else{
                BaseResult.Error(Failure(response.status, response.message))
            }
        }catch(e: NoInternetException){
            return BaseResult.Error(Failure(0, e.message))
        }catch(e: Exception){
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

    suspend fun updatePlan(plan_id:Long,plan: PlanUpdateRequest): BaseResult<PlanEntity, Failure>{
        try {
            val response = planApi.updatePlan(plan_id,plan)
            return if (response.status == 200) {
                val plan = response.data?.let { t ->
                    PlanEntity(t.id, t.planner_idFK, t.todo, t.rgb, t.time, t.location, t.date)
                }
                BaseResult.Success(plan!!)
            }else{
                BaseResult.Error(Failure(response.status, response.message))
            }
        }catch(e: NoInternetException){
            return BaseResult.Error(Failure(0, e.message))
        }catch(e: Exception){
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

}