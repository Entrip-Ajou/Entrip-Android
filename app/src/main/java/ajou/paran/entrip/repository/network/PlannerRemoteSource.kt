package ajou.paran.entrip.repository.network

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.api.PlanApi
import ajou.paran.entrip.repository.network.dto.PlannerUpdateRequest
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import android.util.Log
import retrofit2.HttpException
import javax.inject.Inject

class PlannerRemoteSource
@Inject constructor(
    private val planApi: PlanApi
) {
    companion object {
        const val TAG = "[PlannerRemote]"
    }

    /**
     * @POST : save(2ntrip.com/api/v1/planners)
     * @RequestBody : String user_id
     * **/
    suspend fun createPlanner(userId: String): BaseResult<PlannerEntity, Failure> {
        try {
            val response = planApi.createPlanner(userId)
            return if (response.status == 200) {
                val planner = response.data?.let { t ->
                    PlannerEntity(
                        t.planner_id,
                        t.title,
                        t.start_date,
                        t.end_date,
                        t.timeStamp,
                        t.comment_timeStamp
                    )
                }
                BaseResult.Success(planner!!)
            } else {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        } catch (e: NoInternetException) {
            Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
            return BaseResult.Error(Failure(0, e.message))
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException Message = " + e.localizedMessage)
            return BaseResult.Error(Failure(e.code(), e.message()))
        } catch (e: Exception) {
            Log.e(TAG, "Exception Message = " + e.localizedMessage)
            return BaseResult.Error(Failure(-1, e.message.toString()))
        }
    }

    /**
     * @PUT : 2ntrip.com/api/v1/planners/{planner_id}
     * @PathVariable : Long planner_id
     * @RequestBody : String title, String start_date, String end_date
     * **/
    suspend fun updatePlanner(
        plannerId: Long,
        planner: PlannerUpdateRequest
    ): BaseResult<PlannerEntity, Failure> = try {
        val response = planApi.updatePlanner(plannerId, planner)
        if (response.status == 200) {
            val updatePlanner = response.data.let { t ->
                PlannerEntity(
                    planner_id = t.planner_id,
                    title = t.title,
                    start_date = t.start_date,
                    end_date = t.end_date,
                    time_stamp = t.time_stamp,
                    comment_timeStamp = t.comment_timeStamp
                )
            }
            BaseResult.Success(updatePlanner)
        } else {
            Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
            BaseResult.Error(Failure(response.status, response.message))
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }


    /**
     * @GET : findById (2ntrip.com/api/v1/planners/{planner_id})
     * @PathVariable : Long planner_id
     * **/
    suspend fun findPlanner(plannerId: Long): BaseResult<PlannerEntity, Failure> =
        try {
            val response = planApi.fetchPlanner(plannerId)
            if (response.status == 200) {
                val planner = response.data?.let { t ->
                    PlannerEntity(
                        t.planner_id,
                        t.title,
                        t.start_date,
                        t.end_date,
                        t.timeStamp,
                        t.comment_timeStamp
                    )
                }
                BaseResult.Success(planner!!)
            } else {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        } catch (e: NoInternetException) {
            Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
            BaseResult.Error(Failure(0, e.message))
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException Message = " + e.localizedMessage)
            BaseResult.Error(Failure(e.code(), e.message()))
        } catch (e: Exception) {
            Log.e(TAG, "Exception Message = " + e.localizedMessage)
            BaseResult.Error(Failure(-1, e.message.toString()))
        }

    /**
     *  Home ?????? -> Planner ??? ???????????? ???????????? ????????????
     *  planner list ??? planner??? ???????????? ??? ???????????? ??????
     *  isExist -> True : ?????? planner??? ????????? ???????????? planView??? ???????????? sync??????
     *          -> False : ?????? planner??? ????????? ???????????? planView??? ???????????? ??????,
     *                     Home ????????? Planner??? ????????? ???????????? ??????.
     */
    suspend fun isExist(planner_id: Long): BaseResult<Boolean, Failure> = try {
        val response = planApi.isExist(planner_id)
        if (response.status == 200) {
            BaseResult.Success(response.data)
        } else {
            Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
            BaseResult.Error(Failure(response.status, response.message))
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    /**
     * @DELETE : delete (2ntrip.com/api/v1/planners/{planner_id})
     * @PathVariable : Long planner_id
     * @Limit OrphanRemoval = true ????????? Planners??? Join???????????? ?????? Plans ?????? ??????
     * **/
    suspend fun deletePlanner(planner_id: Long): BaseResult<Long, Failure> = try {
        val response1 = planApi.isExist(planner_id)
        var isExist: Boolean
        if (response1.status == 200) {
            isExist = response1.data
            if (isExist) {
                val response2 = planApi.deletePlanner(planner_id)
                if (response2.status == 200) BaseResult.Success(response2.data)
                else {
                    Log.e(
                        TAG,
                        "Err code = " + response2.status + " Err message = " + response2.message
                    )
                    BaseResult.Error(Failure(response2.status, response2.message))
                }
            } else {
                // ???????????? ?????? ??????(?????? ???????????? ?????? ?????????)
                BaseResult.Success(planner_id)
            }
        } else {
            Log.e(TAG, "Err code = " + response1.status + " Err message = " + response1.message)
            BaseResult.Error(Failure(response1.status, response1.message))
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun fetchPlans(planner_idFK: Long): BaseResult<List<PlanEntity>, Failure> =
        try {
            val response = planApi.fetchPlans(planner_idFK)
            if (response.status == 200) {
                val plans = mutableListOf<PlanEntity>()
                response.data?.forEach { t ->
                    plans.add(
                        PlanEntity(
                            t.id,
                            t.planner_idFK,
                            t.todo,
                            t.rgb,
                            t.time,
                            t.location,
                            t.date,
                            t.isExistComments
                        )
                    )
                }
                BaseResult.Success(plans)
            } else {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        } catch (e: NoInternetException) {
            Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
            BaseResult.Error(Failure(0, e.message))
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException Message = " + e.localizedMessage)
            BaseResult.Error(Failure(e.code(), e.message()))
        } catch (e: Exception) {
            Log.e(TAG, "Exception Message = " + e.localizedMessage)
            BaseResult.Error(Failure(-1, e.message.toString()))
        }


    suspend fun fetchPlanner(planner_id: Long): BaseResult<PlannerEntity, Failure> =
        try {
            val response = planApi.fetchPlanner(planner_id)
            if (response.status == 200) {
                val planner = response.data?.let { t ->
                    PlannerEntity(
                        t.planner_id,
                        t.title,
                        t.start_date,
                        t.end_date,
                        t.timeStamp,
                        t.comment_timeStamp
                    )
                }
                BaseResult.Success(planner!!)
            } else {
                BaseResult.Error(Failure(response.status, response.message))
            }
        } catch (e: NoInternetException) {
            Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
            BaseResult.Error(Failure(0, e.message))
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException Message = " + e.localizedMessage)
            BaseResult.Error(Failure(e.code(), e.message()))
        } catch (e: Exception) {
            Log.e(TAG, "Exception Message = " + e.localizedMessage)
            BaseResult.Error(Failure(-1, e.message.toString()))
        }
}