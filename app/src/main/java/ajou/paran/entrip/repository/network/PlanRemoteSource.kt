package ajou.paran.entrip.repository.network

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.api.PlanApi
import ajou.paran.entrip.repository.network.api.TokenApi
import ajou.paran.entrip.repository.network.dto.PlanRequest
import ajou.paran.entrip.repository.network.dto.PlanUpdateRequest
import ajou.paran.entrip.repository.network.dto.response.UserReissueAccessTokenResponseDto
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import android.content.SharedPreferences
import android.util.Log
import retrofit2.HttpException
import javax.inject.Inject

class PlanRemoteSource
@Inject constructor(
    private val planApi: PlanApi,
    private val tokenApi: TokenApi,
    private val sharedPreferences: SharedPreferences
) {
    companion object{
        private const val TAG = "[PlanRemote]"
    }

    suspend fun fetchPlanner(
        planner_id: Long
    ): BaseResult<PlannerEntity, Failure> = try {
        val response = planApi.fetchPlanner(planner_id)
        when (response.status) {
            200 -> {
                BaseResult.Success(
                    response.data.let { t ->
                        PlannerEntity(
                            t.planner_id,
                            t.title,
                            t.start_date,
                            t.end_date,
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

    suspend fun insertPlan(
        plan: PlanRequest
    ): BaseResult<PlanEntity, Failure> = try {
        val response = planApi.insertPlan(plan)
        when (response.status) {
            200 -> {
                BaseResult.Success(
                    response.data.let { t ->
                        PlanEntity(
                            id = t.id,
                            planner_idFK = t.planner_idFK,
                            todo = t.todo,
                            time = t.time,
                            location = t.location,
                            date = t.date,
                            isExistComments = t.isExistComments
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
                        insertPlan(plan)
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

    suspend fun deletePlan(
        plan_id: Long
    ): BaseResult<Long, Failure> = try {
        val response = planApi.deletePlan(plan_id)
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
        when (e.code()) {
            400 -> {
                when (val response = reissueUserAccessToken()) {
                    is BaseResult.Success -> {
                        deletePlan(plan_id)
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

    suspend fun updatePlan(
        plan_id: Long,
        plan: PlanUpdateRequest
    ): BaseResult<PlanEntity, Failure> = try {
        val response = planApi.updatePlan(plan_id, plan)
        when (response.status) {
            200 -> {
                BaseResult.Success(
                    response.data.let { t ->
                        PlanEntity(
                            id = t.id,
                            planner_idFK = t.planner_idFK,
                            todo = t.todo,
                            time = t.time,
                            location = t.location,
                            date = t.date,
                            isExistComments = t.isExistComments
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
                        updatePlan(plan_id, plan)
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
    ): BaseResult<String, Failure> = try {
        val response = tokenApi.reissueUserAccessToken(refreshToken = refreshToken)
        when (response.status) {
            200 -> {
                sharedPreferences.edit().putString("accessToken", response.data).commit()
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