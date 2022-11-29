package ajou.paran.entrip.repository.network

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.api.PlanApi
import ajou.paran.entrip.repository.network.api.TokenApi
import ajou.paran.entrip.repository.network.dto.PlannerUpdateRequest
import ajou.paran.entrip.repository.network.dto.response.UserReissueAccessTokenResponseDto
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import android.content.SharedPreferences
import android.util.Log
import retrofit2.HttpException
import javax.inject.Inject

class PlannerRemoteSource
@Inject constructor(
    private val planApi: PlanApi,
    private val tokenApi: TokenApi,
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val TAG = "[PlannerRemote]"
    }

    /**
     * @POST : save(2ntrip.com/api/v1/planners)
     * @RequestBody : String user_id
     * **/
    suspend fun createPlanner(
        userId: String
    ): BaseResult<PlannerEntity, Failure> = try {
        val response = planApi.createPlanner(userId)
        when (response.status) {
            200 -> {
                BaseResult.Success(
                    data = response.data.let { t ->
                        PlannerEntity(
                            planner_id = t.planner_id,
                            title = t.title,
                            start_date = t.start_date,
                            end_date = t.end_date,
                        )
                    }
                )
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        when (e.code()) {
            400 -> {
                when (val response = reissueUserAccessToken()) {
                    is BaseResult.Success -> {
                        createPlanner(userId)
                    }
                    is BaseResult.Error -> {
                        BaseResult.Error(response.err)
                    }
                }
            }
            else -> {
                BaseResult.Error(Failure(e.code(), e.message()))
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
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
            BaseResult.Success(
                data = response.data.let { t ->
                    PlannerEntity(
                        planner_id = t.planner_id,
                        title = t.title,
                        start_date = t.start_date,
                        end_date = t.end_date,
                    )
                }
            )
        } else {
            Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
            BaseResult.Error(Failure(response.status, response.message))
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        when (e.code()) {
            400 -> {
                when (val response = reissueUserAccessToken()) {
                    is BaseResult.Success -> {
                        updatePlanner(plannerId, planner)
                    }
                    is BaseResult.Error -> {
                        BaseResult.Error(response.err)
                    }
                }
            }
            else -> {
                BaseResult.Error(Failure(e.code(), e.message()))
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    /**
     *  Home 화면 -> Planner 로 넘어가는 과정에서 사용자가
     *  planner list 중 planner를 선택했을 때 호출되는 함수
     *  isExist -> True : 해당 planner가 서버에 있으므로 planView로 넘어가서 sync작업
     *          -> False : 해당 planner가 서버에 없으므로 planView로 넘어가지 않고,
     *                     Home 화면에 Planner가 없다고 알려줘야 한다.
     */
    suspend fun isExist(
        planner_id: Long
    ): BaseResult<Boolean, Failure> = try {
        val response = planApi.isExist(planner_id)
        when (response.status) {
            200 -> {
                BaseResult.Success(response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
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
     * @Limit OrphanRemoval = true 때문에 Planners와 Join되어있는 모든 Plans 역시 삭제
     * **/
    suspend fun deletePlanner(
        user_id : String,
        planner_id: Long
    ): BaseResult<Boolean, Failure> = try {
        val response = planApi.deletePlanner(user_id, planner_id)
        if(response.status == 200){
            BaseResult.Success(response.data)
        }else {
            Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
            BaseResult.Error(Failure(response.status, response.message))
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        when (e.code()) {
            400 -> {
                when (val response = reissueUserAccessToken()) {
                    is BaseResult.Success -> {
                        deletePlanner(user_id, planner_id)
                    }
                    is BaseResult.Error -> {
                        BaseResult.Error(response.err)
                    }
                }
            }
            else -> {
                BaseResult.Error(Failure(e.code(), e.message()))
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun fetchPlans(
        planner_idFK: Long
    ): BaseResult<List<PlanEntity>, Failure> = try {
        val response = planApi.fetchPlans(planner_idFK)
        when (response.status) {
            200 -> {
                val plans = mutableListOf<PlanEntity>()
                response.data.forEach { t ->
                    plans.add(
                        PlanEntity(
                            id = t.id,
                            planner_idFK = t.planner_idFK,
                            todo = t.todo,
                            time = t.time,
                            location = t.location,
                            date = t.date,
                            isExistComments = t.isExistComments
                        )
                    )
                }
                BaseResult.Success(plans)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        when (e.code()) {
            400 -> {
                when (val response = reissueUserAccessToken()) {
                    is BaseResult.Success -> {
                        fetchPlans(planner_idFK)
                    }
                    is BaseResult.Error -> {
                        BaseResult.Error(response.err)
                    }
                }
            }
            else -> {
                BaseResult.Error(Failure(e.code(), e.message()))
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun fetchPlanner(
        planner_id: Long
    ): BaseResult<PlannerEntity, Failure> = try {
        val response = planApi.fetchPlanner(planner_id)
        when (response.status) {
            200 -> {
                BaseResult.Success(response.data
                    .let { t ->
                        PlannerEntity(
                            planner_id = t.planner_id,
                            title = t.title,
                            start_date = t.start_date,
                            end_date = t.end_date,
                        )
                    }
                )
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        when (e.code()) {
            400 -> {
                when (val response = reissueUserAccessToken()) {
                    is BaseResult.Success -> {
                        fetchPlanner(planner_id)
                    }
                    is BaseResult.Error -> {
                        BaseResult.Error(response.err)
                    }
                }
            }
            else -> {
                BaseResult.Error(Failure(e.code(), e.message()))
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun findByPlannerIdWithDate(
        planner_id : Long,
        date : String
    ) : BaseResult<List<PlanEntity>, Failure> = try {
        val response = planApi.findByPlannerIdWithDate(planner_id, date)
        when (response.status) {
            200 -> {
                val plans = mutableListOf<PlanEntity>()
                response.data.forEach { t ->
                    plans.add(
                        PlanEntity(
                            id = t.id,
                            planner_idFK = t.planner_idFK,
                            todo = t.todo,
                            time = t.time,
                            location = t.location,
                            date = t.date,
                            isExistComments = t.isExistComments
                        )
                    )
                }
                BaseResult.Success(plans)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = " + e.localizedMessage)
        when (e.code()) {
            400 -> {
                when (val response = reissueUserAccessToken()) {
                    is BaseResult.Success -> {
                        findByPlannerIdWithDate(planner_id, date)
                    }
                    is BaseResult.Error -> {
                        BaseResult.Error(response.err)
                    }
                }
            }
            else -> {
                BaseResult.Error(Failure(e.code(), e.message()))
            }
        }
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = " + e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    private suspend fun reissueUserAccessToken(
        refreshToken: String = sharedPreferences.getString("refreshToken", "").toString()
    ): BaseResult<UserReissueAccessTokenResponseDto, Failure> = try {
        val response = tokenApi.reissueUserAccessToken(refreshToken = refreshToken)
        when (response.status) {
            200 -> {
                sharedPreferences.edit().putString("accessToken", response.data.accessToken).commit()
                BaseResult.Success(response.data)
            }
            else -> {
                Log.e(TAG, "Networking Message = ${response.message}")
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(e.code(), e.message()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(-1, e.message.toString()))
    }
}