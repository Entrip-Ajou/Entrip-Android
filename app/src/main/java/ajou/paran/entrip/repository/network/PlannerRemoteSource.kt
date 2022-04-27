package ajou.paran.entrip.repository.network

import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.api.PlanApi
import ajou.paran.entrip.repository.network.dto.planner.CreatePlannerDto
import ajou.paran.entrip.repository.network.dto.planner.UpdatePlannerRequestDto
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException

class PlannerRemoteSource
constructor(private val planApi: PlanApi)
{
    /**
     * @POST : save(2ntrip.com/api/v1/planners)
     * @RequestBody : String user_id
     * **/
    suspend fun createPlanner(user_id: String): BaseResult<PlannerEntity, Failure>
            = try {
        val obj = CreatePlannerDto(user_id)
        val response = planApi.createPlanner(obj)
        if (response.httpStatus == "OK") {
            val planner = response.data.let { t ->
                PlannerEntity(
                    t.planner_id,
                    t.title,
                    t.start_date,
                    t.end_date,
                    t.timeStamp
                )
            }
            BaseResult.Success(planner)
        } else {
            BaseResult.Error(Failure(500, response.message))
        }
    } catch (e: NoInternetException) {
        BaseResult.Error(Failure(0, e.message))
    } catch (e: Exception) {
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    /**
     * @PUT : 2ntrip.com/api/v1/planners/{planner_id}
     * @PathVariable : Long planner_id
     * @RequestBody : String title, String start_date, String end_date
     * **/
    suspend fun updatePlanner(plannerId: Long, planner: PlannerEntity): BaseResult<PlannerEntity, Failure>
            = try {
        val response = planApi.updatePlanner(
            plannerId,
            UpdatePlannerRequestDto(
                title = planner.title,
                start_date = planner.start_date,
                end_date = planner.end_date
            )
        )
        if (response.httpStatus == "OK") {
            val updatePlanner = response.data.let { t ->
                PlannerEntity(
                    planner_id = t.planner_id,
                    title = t.title,
                    start_date = t.start_date,
                    end_date = t.end_date,
                    timeStamp = t.timeStamp)
            }
            BaseResult.Success(updatePlanner)
        }else{
            BaseResult.Error(Failure(500, response.message))
        }
    }catch(e: NoInternetException){
        BaseResult.Error(Failure(0, e.message))
    }catch(e: Exception){
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    /**
     * @GET : findById (2ntrip.com/api/v1/planners/{planner_id})
     * @PathVariable : Long planner_id
     * **/
    suspend fun findPlanner(plannerId: Long): BaseResult<PlannerEntity, Failure>
            = try {
        val response = planApi.findPlanner(plannerId)
        if (response.httpStatus == "OK") {
            val planner = response.data.let { t ->
                PlannerEntity(
                    planner_id = t.planner_id,
                    title = t.title,
                    start_date = t.start_date,
                    end_date = t.end_date,
                    timeStamp = t.timeStamp
                )
            }
            BaseResult.Success(planner)
        } else {
            BaseResult.Error(Failure(500, response.message))
        }
    } catch (e: NoInternetException) {
        BaseResult.Error(Failure(0, e.message))
    } catch (e: Exception) {
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    /**
     * @GET : plannerIsExistWithId(2ntrip.com/api/v1/planners/{planner_id}/exist)
     * @PathVariable : Long planner_id
     * **/
    suspend fun isExist(planner_id: Long): BaseResult<Boolean, Failure>
            = try {
        val response = planApi.isExist(planner_id)
        if (response.httpStatus == "OK") {
            BaseResult.Success(response.data)
        } else {
            BaseResult.Error(Failure(500, response.message))
        }
    } catch (e: NoInternetException) {
        BaseResult.Error(Failure(0, e.message))
    } catch (e: Exception) {
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    /**
     * @DELETE : delete (2ntrip.com/api/v1/planners/{planner_id})
     * @PathVariable : Long planner_id
     * @Limit OrphanRemoval = true 때문에 Planners와 Join되어있는 모든 Plans 역시 삭제
     * **/
    suspend fun deletePlanner(planner_id: Long) : BaseResult<Long, Failure>
            = try{
        val response = planApi.deletePlanner(planner_id)
        if (response.httpStatus == "OK"){
            BaseResult.Success(response.data)
        } else {
            BaseResult.Error(Failure(500, response.message))
        }
    } catch (e: NoInternetException) {
        BaseResult.Error(Failure(0, e.message))
    } catch (e: Exception) {
        BaseResult.Error(Failure(-1, e.message.toString()))
    }
}