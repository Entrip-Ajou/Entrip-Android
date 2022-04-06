package ajou.paran.entrip.repository.network

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.api.PlanApi
import ajou.paran.entrip.repository.network.dto.PlanRequest
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException

class PlanRemoteSource constructor(private val planApi: PlanApi) {
    /**
     *  Home 화면 -> Planner 로 넘어가는 과정에서 사용자가
     *  planner list 중 planner를 선택했을 때 호출되는 함수
     *  isExist -> True : 해당 planner가 서버에 있으므로 planView로 넘어가서 sync작업
     *          -> False : 해당 planner가 서버에 없으므로 planView로 넘어가지 않고,
     *                     Home 화면에 Planner가 없다고 알려줘야 한다.
     */
    suspend fun isExist(planner_id: Long): BaseResult<Boolean, Failure> {
        try {
            val response = planApi.isExist(planner_id)
            return if (response.isSuccessful) {
                BaseResult.Success(response.body()!!)
            } else {
                BaseResult.Error(Failure(response.code(), response.message()))
            }
        } catch (e: NoInternetException) {
            return BaseResult.Error(Failure(0, e.message))
        } catch (e: Exception) {
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

    suspend fun createPlanner(): BaseResult<PlannerEntity, Failure> {
        try {
            val response = planApi.createPlanner()
            return if (response.isSuccessful) {
                val planner = response.body()?.let { t ->
                    PlannerEntity(t.planner_id, t.title, t.start_date, t.end_date, t.timeStamp)
                }
                BaseResult.Success(planner!!)
            } else {
                BaseResult.Error(Failure(response.code(), response.message()))
            }
        } catch (e: NoInternetException) {
            return BaseResult.Error(Failure(0, e.message))
        } catch (e: Exception) {
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

    suspend fun fetchPlans(planner_idFK: Long): BaseResult<List<PlanEntity>, Failure> {
        try {
            val response = planApi.fetchPlans(planner_idFK)
            return if (response.isSuccessful) {
                val plans = mutableListOf<PlanEntity>()
                response.body()?.forEach { t ->
                    plans.add(
                        PlanEntity(t.id, t.planner_idFK, t.todo, t.rgb, t.time, t.location, t.date)
                    )
                }
                BaseResult.Success(plans)
            } else {
                BaseResult.Error(Failure(response.code(), response.message()))
            }
        } catch (e: NoInternetException) {
            return BaseResult.Error(Failure(0, e.message))
        } catch (e: Exception) {
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

    suspend fun fetchPlanner(planner_id: Long): BaseResult<PlannerEntity, Failure> {
        try {
            val response = planApi.fetchPlanner(planner_id)
            return if (response.isSuccessful) {
                val planner = response.body()?.let { t ->
                    PlannerEntity(t.planner_id, t.title, t.start_date, t.end_date, t.timeStamp)
                }
                BaseResult.Success(planner!!)
            } else {
                BaseResult.Error(Failure(response.code(), response.message()))
            }
        } catch (e: NoInternetException) {
            return BaseResult.Error(Failure(0, e.message))
        } catch (e: Exception) {
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

    /**         Return Response             Send Request(parameter)
     * insert - PlanEntity                  PlanRequest
     * delete - 지웠던 plan id               plan id
     * update - PlanEntity                  planEntity
     */
    suspend fun insertPlan(plan: PlanRequest): BaseResult<PlanEntity, Failure> {
        try {
            val response = planApi.insertPlan(plan)
            return if (response.isSuccessful) {
                val plan = response.body()?.let { t ->
                    PlanEntity(t.id, t.planner_idFK, t.todo, t.rgb, t.time, t.location, t.date)
                }
                BaseResult.Success(plan!!)
            }else{
                BaseResult.Error(Failure(response.code(), response.message()))
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
            return if(response.isSuccessful){
                BaseResult.Success(response.body()!!)
            }else{
                BaseResult.Error(Failure(response.code(), response.message()))
            }
        }catch(e: NoInternetException){
            return BaseResult.Error(Failure(0, e.message))
        }catch(e: Exception){
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

    suspend fun updatePlan(plan_id:Long,plan: PlanEntity): BaseResult<PlanEntity, Failure>{
        try {
            val response = planApi.updatePlan(plan_id,plan)
            return if (response.isSuccessful) {
                val plan = response.body()?.let { t ->
                    PlanEntity(t.id, t.planner_idFK, t.todo, t.rgb, t.time, t.location, t.date)
                }
                BaseResult.Success(plan!!)
            }else{
                BaseResult.Error(Failure(response.code(), response.message()))
            }
        }catch(e: NoInternetException){
            return BaseResult.Error(Failure(0, e.message))
        }catch(e: Exception){
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

}