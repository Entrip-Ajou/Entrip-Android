package ajou.paran.entrip.repository.network

import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.api.PlanApi
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException

class PlannerRemoteSource constructor(private val planApi: PlanApi) {
    /**
     * @POST : save(2ntrip.com/api/v1/planners)
     * @RequestBody : String user_id
     * **/
    suspend fun createPlanner(user_id : String): BaseResult<PlannerEntity, Failure> {
        try {
            val response = planApi.createPlanner(user_id)
            return if (response.status == 200) {
                val planner = response.data?.let { t ->
                    PlannerEntity(t.planner_id, t.title, t.start_date, t.end_date, t.timeStamp, t.user_timeStamp, t.comment_timeStamp)
                }
                BaseResult.Success(planner!!)
            } else {
                BaseResult.Error(Failure(500, response.message))
            }
        } catch (e: NoInternetException) {
            return BaseResult.Error(Failure(0, e.message))
        } catch (e: Exception) {
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
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
        if (response.httpStatus == 200) {
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
    suspend fun findPlanner(plannerId: Long): BaseResult<PlannerEntity, Failure> {
        try {
            val response = planApi.fetchPlanner(plannerId)
            return if (response.status == 200) {
                val planner = response.data?.let { t ->
                    PlannerEntity(t.planner_id, t.title, t.start_date, t.end_date, t.timeStamp, t.user_timeStamp, t.comment_timeStamp)
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


    /**
     *  Home 화면 -> Planner 로 넘어가는 과정에서 사용자가
     *  planner list 중 planner를 선택했을 때 호출되는 함수
     *  isExist -> True : 해당 planner가 서버에 있으므로 planView로 넘어가서 sync작업
     *          -> False : 해당 planner가 서버에 없으므로 planView로 넘어가지 않고,
     *                     Home 화면에 Planner가 없다고 알려줘야 한다.
     */
    suspend fun isExist(planner_id: Long): BaseResult<Boolean, Failure>
            = try {
        val response = planApi.isExist(planner_id)
        if (response.status == 200) {
            BaseResult.Success(response.data)
        } else {
            BaseResult.Error(Failure(response.status, response.message))
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
        val response1 = planApi.isExist(planner_id)
        var isExist : Boolean
        if(response1.status == 200){
            isExist = response1.data
            if(isExist){
                val response2 = planApi.deletePlanner(planner_id)
                if(response2.status == 200) BaseResult.Success(response2.data)
                else BaseResult.Error(Failure(response2.status, response2.message))
            }else{
                // 존재하지 않는 경우(다른 사용자가 이미 삭제함)
                BaseResult.Success(planner_id)
            }
        }else{
            BaseResult.Error(Failure(response1.status, response1.message))
        }
    } catch (e: NoInternetException) {
        BaseResult.Error(Failure(0, e.message))
    } catch (e: Exception) {
        BaseResult.Error(Failure(-1, e.message.toString()))
    }
}