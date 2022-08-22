package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.model.PlanEntity
import ajou.paran.entrip.model.PlannerEntity
import ajou.paran.entrip.repository.network.CommentRemoteSource
import ajou.paran.entrip.repository.network.PlannerRemoteSource
import ajou.paran.entrip.repository.network.UserRemoteSource
import ajou.paran.entrip.repository.network.dto.PlannerResponse
import ajou.paran.entrip.repository.network.dto.UserRequest
import ajou.paran.entrip.repository.network.dto.UserResponse
import ajou.paran.entrip.repository.network.dto.UserTemp
import ajou.paran.entrip.repository.network.dto.*
import ajou.paran.entrip.repository.room.plan.dao.PlanDao
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class UserRepositoryImpl
@Inject
constructor(
    private val userRemoteSource: UserRemoteSource,
    private val plannerRemoteSource: PlannerRemoteSource,
    private val planDao: PlanDao
) : UserRepository {

    companion object {
        const val TAG = "[UserImpl]"
    }

    override fun getUserPlanners(user_id: String): Flow<BaseResult<List<PlannerResponse>, Failure>>
    = flow {
            try {
                val response = userRemoteSource.getUserPlanners(user_id)
                when(response.status) {
                    200 -> {
                        for (data in response.data) {
                            val plannerEntity = PlannerEntity(
                                planner_id = data.planner_id,
                                title = data.title,
                                start_date = data.start_date,
                                end_date = data.end_date,
                            )
                            planDao.insertPlanner(plannerEntity)
                            when (val remoteDB_plan =
                                plannerRemoteSource.fetchPlans(plannerEntity.planner_id)) {
                                is BaseResult.Success -> {
                                    savePlanToLocal(remoteDB_plan.data, plannerEntity.planner_id)
                                }
                                is BaseResult.Error -> {
                                    Log.e(
                                        TAG,
                                        "Err code = " + response.status + " Err message = " + response.message
                                    )
                                    emit(
                                        BaseResult.Error(
                                            Failure(
                                                code = remoteDB_plan.err.code,
                                                message = remoteDB_plan.err.message
                                            )
                                        )
                                    )
                                }
                            }
                        }
                        emit(BaseResult.Success(response.data))
                    }
                    else -> {
                        Log.e(TAG, "Err code = "+response.status+ " Err message = " + response.message)
                        emit(BaseResult.Error(Failure(response.status, response.message)))
                    }
                }
            } catch (e: NoInternetException) {
                Log.e(TAG, "NoInternetException Message = "+e.localizedMessage)
                emit(BaseResult.Error(Failure(0, e.message)))
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException Message = "+e.localizedMessage)
                emit(BaseResult.Error(Failure(e.code(), e.message.toString())))
            } catch (e: Exception) {
                Log.e(TAG, "Exception Message = "+e.localizedMessage)
                emit(BaseResult.Error(Failure(-1, e.message.toString())))
            }
        }

    override suspend fun getUserPlannersResult(user_id: String): BaseResult<List<PlannerResponse>, Failure>
    = try {
        val response = userRemoteSource.getUserPlanners(user_id)
        when(response.status) {
            200 -> {
                for (data in response.data) {
                    val plannerEntity = PlannerEntity(
                        planner_id = data.planner_id,
                        title = data.title,
                        start_date = data.start_date,
                        end_date = data.end_date,
                        time_stamp = data.timeStamp,
                        comment_timeStamp = data.comment_timeStamp
                    )
                    val localPlanner = planDao.findPlanner(plannerEntity.planner_id)

                    when (val localTimestamp: String? = localPlanner?.time_stamp) {
                        null -> {
                            planDao.insertPlanner(plannerEntity)

                            when (val remoteDB_plan = plannerRemoteSource.fetchPlans(plannerEntity.planner_id)) {
                                is BaseResult.Success -> { savePlanToLocal(remoteDB_plan.data, plannerEntity.planner_id) }
                                is BaseResult.Error -> {
                                    Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)

                                    BaseResult.Error(
                                        Failure(
                                            code = remoteDB_plan.err.code,
                                            message = remoteDB_plan.err.message
                                        )
                                    )
                                }
                            }
                        }
                        else -> {
                            if (plannerEntity.time_stamp != localTimestamp) {
                                // 최신 상태 x -> remoteDB fetch
                                planDao.updatePlanner(plannerEntity)
                                when (val remoteDB_plan = plannerRemoteSource.fetchPlans(plannerEntity.planner_id)) {
                                    is BaseResult.Success -> { savePlanToLocal(remoteDB_plan.data, plannerEntity.planner_id) }
                                    is BaseResult.Error -> {
                                        Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)

                                        BaseResult.Error(
                                            Failure(
                                                code = remoteDB_plan.err.code,
                                                message = remoteDB_plan.err.message
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                BaseResult.Success(response.data)
            }
            else -> {
                Log.e(TAG, "Err code = " + response.status + " Err message = " + response.message)
                BaseResult.Error(Failure(response.status, response.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = "+e.localizedMessage)
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = "+e.localizedMessage)
        BaseResult.Error(Failure(e.code(), e.message.toString()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = "+e.localizedMessage)
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    override fun isExistUserId(user_id: String): Flow<BaseResult<Boolean, Failure>>
    = flow {
        try {
            val response = userRemoteSource.isExistUserId(user_id)
            when(response.status) {
                200 -> { emit(BaseResult.Success(response.data)) }
                202 -> { emit(BaseResult.Success(response.data)) }
                else -> {
                    Log.e(TAG, "Err code = "+response.status+ " Err message = " + response.message)
                    emit(BaseResult.Error(Failure(response.status, response.message)))
                }
            }
        } catch (e: NoInternetException) {
            Log.e(TAG, "NoInternetException Message = "+e.localizedMessage)
            emit(BaseResult.Error(Failure(0, e.message)))
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException Message = "+e.localizedMessage)
            emit(BaseResult.Error(Failure(e.code(), e.message.toString())))
        } catch (e: Exception) {
            Log.e(TAG, "Exception Message = "+e.localizedMessage)
            emit(BaseResult.Error(Failure(-1, e.message.toString())))
        }
    }

    override fun isExistNickname(nickname: String): Flow<BaseResult<Boolean, Failure>>
    = flow {
        try {
            val response = userRemoteSource.isExistNickname(nickname)
            when(response.status) {
                200 -> { emit(BaseResult.Success(response.data)) }
                202 -> { emit(BaseResult.Success(response.data)) }
                else -> {
                    Log.e(TAG, "Err code = "+response.status+ " Err message = " + response.message)
                    emit(BaseResult.Error(Failure(response.status, response.message)))
                }
            }
        } catch (e: NoInternetException) {
            Log.e(TAG, "NoInternetException Message = "+e.localizedMessage)
            emit(BaseResult.Error(Failure(0, e.message)))
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException Message = "+e.localizedMessage)
            emit(BaseResult.Error(Failure(e.code(), e.message.toString())))
        } catch (e: Exception) {
            Log.e(TAG, "Exception Message = "+e.localizedMessage)
            emit(BaseResult.Error(Failure(-1, e.message.toString())))
        }
    }

    override fun saveUser(
        user_id: String,
        gender: Int,
        nickname: String
    ): Flow<BaseResult<UserRequest, Failure>>
    = flow {
        try {
            val initial_photoUrl =
                "https://user-images.githubusercontent.com/77181865/169517449-f000a59d-5659-4957-9cb4-c6e5d3f4b197.png"
            val userResponse = UserTemp(user_id, gender, nickname, initial_photoUrl)

            val response = userRemoteSource.saveUser(userResponse)
            when(response.status) {
                200 -> {
                    val dto = response.data
                    emit(BaseResult.Success(dto))
                }
                else -> {
                    Log.e(TAG, "Err code = "+response.status+ " Err message = " + response.message)
                    emit(BaseResult.Error(Failure(response.status, response.message)))
                }
            }
        } catch (e: NoInternetException) {
            Log.e(TAG, "NoInternetException Message = "+e.localizedMessage)
            emit(BaseResult.Error(Failure(0, e.message)))
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException Message = "+e.localizedMessage)
            emit(BaseResult.Error(Failure(e.code(), e.message.toString())))
        } catch (e: Exception) {
            Log.e(TAG, "Exception Message = "+e.localizedMessage)
            emit(BaseResult.Error(Failure(-1, e.message.toString())))
        }
    }

    override fun findById(user_id: String): Flow<BaseResult<UserResponse, Failure>>
    = flow {
        try {
            val response = userRemoteSource.findById(user_id)
            when(response.status) {
                200 -> { emit(BaseResult.Success(response.data)) }
                else -> {
                    Log.e(TAG, "Err code = "+response.status+ " Err message = " + response.message)
                    emit(BaseResult.Error(Failure(response.status, response.message)))
                }
            }
        } catch (e: NoInternetException) {
            Log.e(TAG, "NoInternetException Message = "+e.localizedMessage)
            emit(BaseResult.Error(Failure(0, e.message)))
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException Message = "+e.localizedMessage)
            emit(BaseResult.Error(Failure(e.code(), e.message.toString())))
        } catch (e: Exception) {
            Log.e(TAG, "Exception Message = "+e.localizedMessage)
            emit(BaseResult.Error(Failure(-1, e.message.toString())))
        }
    }

    override fun getTrip(user_id: String): Flow<BaseResult<List<TripResponse>, Failure>>
    = flow {
        try {
            val response = userRemoteSource.findByUserId(user_id)
            when(response.status) {
                200 -> {
                    val response2 = userRemoteSource.getListTrip(response.data.travelFavorite)
                    when(response2.status) {
                        200 -> { emit(BaseResult.Success(response2.data)) }
                        else -> {
                            Log.e(TAG, "Err code = "+response.status+ " Err message = " + response.message)
                            emit(BaseResult.Error(Failure(response2.status, response2.message)))
                        }
                    }
                }
                else -> {
                    Log.e(TAG, "Err code = "+response.status+ " Err message = " + response.message)
                    emit(BaseResult.Error(Failure(response.status, response.message)))
                }
            }
        } catch (e: NoInternetException) {
            Log.e(TAG, "NoInternetException Message = "+e.localizedMessage)
            emit(BaseResult.Error(Failure(0, e.message)))
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException Message = "+e.localizedMessage)
            emit(BaseResult.Error(Failure(e.code(), e.message.toString())))
        } catch (e: Exception) {
            Log.e(TAG, "Exception Message = "+e.localizedMessage)
            emit(BaseResult.Error(Failure(-1, e.message.toString())))
        }
    }

    private fun savePlanToLocal(plans: List<PlanEntity>, planner_idFK: Long) {
        planDao.deleteAllPlan(planner_idFK)
        planDao.insertAllPlan(plans)
    }
}