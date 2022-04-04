package ajou.paran.entrip.repository.network

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.api.PlanApi
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException

class PlanRemoteSource constructor(private val planApi : PlanApi) {
    suspend fun createPlanner() : BaseResult<PlannerEntity, Failure>{
        try{
            val response = planApi.createPlanner()
            return if(response.isSuccessful){
                val planner = response.body()?.let { t ->
                    PlannerEntity(t.planner_id, t.title, t.start_date, t.end_date, t.timeStamp)
                }
                BaseResult.Success(planner!!)
            }else{
                BaseResult.Error(Failure(response.code(), response.message()))
            }
        }catch(e : NoInternetException){
            return BaseResult.Error(Failure(0, e.message))
        }catch(e : Exception){
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

    suspend fun fetchPlans(planner_idFK : Long) : BaseResult<List<PlanEntity>, Failure>{
        try{
            val response = planApi.fetchPlans(planner_idFK)
            return if(response.isSuccessful){
                val plans = mutableListOf<PlanEntity>()
                response.body()?.forEach{ t ->
                    plans.add(PlanEntity(t.id, t.planner_idFK, t.todo, t.rgb, t.time, t.location, t.date))
                }
                BaseResult.Success(plans)
            }else{
                BaseResult.Error(Failure(response.code(), response.message()))
            }
        }catch(e : NoInternetException){
            return BaseResult.Error(Failure(0, e.message))
        }catch(e : Exception){
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

    suspend fun fetchPlanner(planner_id : Long) : BaseResult<PlannerEntity, Failure>{
        try{
            val response = planApi.fetchPlanner(planner_id)
            return if(response.isSuccessful){
                val planner = response.body()?.let { t ->
                    PlannerEntity(t.planner_id, t.title, t.start_date, t.end_date, t.timeStamp)
                }
                BaseResult.Success(planner!!)
            }else{
                BaseResult.Error(Failure(response.code(), response.message()))
            }
        }catch(e : NoInternetException){
            return BaseResult.Error(Failure(0, e.message))
        }catch(e : Exception){
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }
}