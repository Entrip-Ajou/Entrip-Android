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
import ajou.paran.entrip.repository.network.dto.request.UserLoginRequestDto
import ajou.paran.entrip.repository.network.dto.request.UserSaveRequestDto
import ajou.paran.entrip.repository.network.dto.response.UserSaveResponseDto
import ajou.paran.entrip.repository.room.plan.dao.PlanDao
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import android.content.SharedPreferences
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
    private val planDao: PlanDao,
    private val sharedPreferences: SharedPreferences
) : UserRepository {

    companion object {
        const val TAG = "[UserImpl]"
    }

    override fun getUserPlanners(user_id: String): Flow<BaseResult<List<PlannerResponse>, Failure>> =
        flow {
            try {
                val response = userRemoteSource.getUserPlanners(user_id)
                when (response.status) {
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
                        Log.e(
                            TAG,
                            "Err code = " + response.status + " Err message = " + response.message
                        )
                        emit(BaseResult.Error(Failure(response.status, response.message)))
                    }
                }
            } catch (e: NoInternetException) {
                Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
                emit(BaseResult.Error(Failure(0, e.message)))
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException Message = " + e.localizedMessage)
                emit(BaseResult.Error(Failure(e.code(), e.message.toString())))
            } catch (e: Exception) {
                Log.e(TAG, "Exception Message = " + e.localizedMessage)
                emit(BaseResult.Error(Failure(-1, e.message.toString())))
            }
        }

    override suspend fun getUserPlannersResult(user_id: String): BaseResult<List<PlannerResponse>, Failure> =
        try {
            val response = userRemoteSource.getUserPlanners(user_id)
            when (response.status) {
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

                                BaseResult.Error(
                                    Failure(
                                        code = remoteDB_plan.err.code,
                                        message = remoteDB_plan.err.message
                                    )
                                )
                            }
                        }
                    }

                    BaseResult.Success(response.data)
                }
                else -> {
                    Log.e(
                        TAG,
                        "Err code = " + response.status + " Err message = " + response.message
                    )
                    BaseResult.Error(Failure(response.status, response.message))
                }
            }
        } catch (e: NoInternetException) {
            Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
            BaseResult.Error(Failure(0, e.message))
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException Message = " + e.localizedMessage)
            BaseResult.Error(Failure(e.code(), e.message.toString()))
        } catch (e: Exception) {
            Log.e(TAG, "Exception Message = " + e.localizedMessage)
            BaseResult.Error(Failure(-1, e.message.toString()))
        }

    override fun saveUser(
        user_id: String,
        gender: Int,
        nickname: String
    ): Flow<BaseResult<UserRequest, Failure>> = flow {
        try {
            val initial_photoUrl =
                "https://user-images.githubusercontent.com/77181865/169517449-f000a59d-5659-4957-9cb4-c6e5d3f4b197.png"
            val userResponse = UserTemp(user_id, gender, nickname, initial_photoUrl)

            val response = userRemoteSource.saveUser(userResponse)
            when (response.status) {
                200 -> {
                    val dto = response.data
                    emit(BaseResult.Success(dto))
                }
                else -> {
                    Log.e(
                        TAG,
                        "Err code = " + response.status + " Err message = " + response.message
                    )
                    emit(BaseResult.Error(Failure(response.status, response.message)))
                }
            }
        } catch (e: NoInternetException) {
            Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
            emit(BaseResult.Error(Failure(0, e.message)))
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException Message = " + e.localizedMessage)
            emit(BaseResult.Error(Failure(e.code(), e.message.toString())))
        } catch (e: Exception) {
            Log.e(TAG, "Exception Message = " + e.localizedMessage)
            emit(BaseResult.Error(Failure(-1, e.message.toString())))
        }
    }

    override fun findById(user_id: String): Flow<BaseResult<UserResponse, Failure>> = flow {
        try {
            val response = userRemoteSource.findById(user_id)
            when (response.status) {
                200 -> {
                    emit(BaseResult.Success(response.data))
                }
                else -> {
                    Log.e(
                        TAG,
                        "Err code = " + response.status + " Err message = " + response.message
                    )
                    emit(BaseResult.Error(Failure(response.status, response.message)))
                }
            }
        } catch (e: NoInternetException) {
            Log.e(TAG, "NoInternetException Message = " + e.localizedMessage)
            emit(BaseResult.Error(Failure(0, e.message)))
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException Message = " + e.localizedMessage)
            emit(BaseResult.Error(Failure(e.code(), e.message.toString())))
        } catch (e: Exception) {
            Log.e(TAG, "Exception Message = " + e.localizedMessage)
            emit(BaseResult.Error(Failure(-1, e.message.toString())))
        }
    }

    private fun savePlanToLocal(plans: List<PlanEntity>, planner_idFK: Long) {
        planDao.deleteAllPlan(planner_idFK)
        planDao.insertAllPlan(plans)
    }

    override suspend fun saveUserAccount(
        userId: String,
        nickname: String,
        photoUrl: String,
        token: String,
        gender: Long,
        password: String,
        attempt: Long
    ): UserSaveResponseDto = when (val response = userRemoteSource.saveUserAccount(
            userSaveRequest = UserSaveRequestDto(
                userId = userId,
                nickname = nickname,
                photoUrl = photoUrl,
                token = token,
                gender = gender,
                password = password
            )
        )
    ) {
        is BaseResult.Success -> {
            response.data
        }
        is BaseResult.Error -> {
            when(response.err.code) {
                0 -> {
                    Log.d(TAG, "Internet Error")
                    when (attempt) {
                        in 0 .. 2 -> {
                            saveUserAccount(userId, nickname, photoUrl, token, gender, password)
                        }
                        else -> {
                            UserSaveResponseDto(
                                userId = userId,
                                nickname = nickname,
                                gender = gender,
                                photoUrl = photoUrl,
                                token = token
                            )
                        }
                    }
                }
                202 -> {
                    Log.d(TAG, "An account that already exists")
                    UserSaveResponseDto(
                        userId = "",
                        nickname = "",
                        gender = -1L,
                        photoUrl = "",
                        token = ""
                    )
                }
                else -> {
                    Log.d(TAG, "Error code = ${response.err.code}, message = ${response.err.message}")
                    UserSaveResponseDto(
                        userId = userId,
                        nickname = nickname,
                        gender = gender,
                        photoUrl = photoUrl,
                        token = token
                    )
                }
            }
        }
    }


    override suspend fun loginUserAccount(
        userId: String,
        password: String,
        attempt: Long
    ): Boolean = when (val response = userRemoteSource.loginUserAccount(loginRequest = UserLoginRequestDto(userId = userId, password = password))) {
        is BaseResult.Success -> {
            sharedPreferences.edit().putString("user_id", response.data.userId).commit()
            sharedPreferences.edit().putString("nickname", response.data.nickname).commit()
            sharedPreferences.edit().putString("accessToken", response.data.accessToken).commit()
            sharedPreferences.edit().putString("refreshToken", response.data.refreshToken).commit()
            true
        }
        is BaseResult.Error -> {
            when(response.err.code) {
                0 -> {
                    Log.d(TAG, "Internet Error")
                    when (attempt) {
                        in 0 .. 2 -> {
                            loginUserAccount(userId, password, attempt + 1)
                        }
                        else -> {
                            false
                        }
                    }
                }
                else -> {
                    Log.d(TAG, "Error code = ${response.err.code}, message = ${response.err.message}")
                    false
                }
            }
        }
    }

    override suspend fun reissueUserAccessToken(
        refreshToken: String,
        attempt: Long
    ): Boolean = when (val response = userRemoteSource.reissueUserAccessToken(refreshToken = refreshToken)) {
        is BaseResult.Success -> {
            sharedPreferences.edit().putString("accessToken", response.data.accessToken).commit()
            true
        }
        is BaseResult.Error -> {
            when(response.err.code) {
                0 -> {
                    Log.d(TAG, "Internet Error")
                    when (attempt) {
                        in 0 .. 2 -> {
                            reissueUserAccessToken(refreshToken, attempt + 1)
                        }
                        else -> {
                            false
                        }
                    }
                }
                else -> {
                    Log.d(TAG, "Error code = ${response.err.code}, message = ${response.err.message}")
                    false
                }
            }
        }
    }

    override suspend fun isExistUserId(
        userId: String,
        attempt: Long
    ): Boolean = when (val response = userRemoteSource.isExistUserId(userId = userId)) {
        is BaseResult.Success -> {
            response.data
        }
        is BaseResult.Error -> {
            when(response.err.code) {
                0 -> {
                    Log.d(TAG, "Internet Error")
                    when (attempt) {
                        in 0 .. 2 -> {
                            isExistUserId(userId, attempt + 1)
                        }
                        else -> {
                            false
                        }
                    }
                }
                else -> {
                    Log.d(TAG, "Error code = ${response.err.code}, message = ${response.err.message}")
                    false
                }
            }
        }
    }

    override suspend fun isExistNickname(
        nickname: String,
        attempt: Long
    ): Boolean = when (val response = userRemoteSource.isExistNickname(nickname = nickname)) {
        is BaseResult.Success -> {
            response.data
        }
        is BaseResult.Error -> {
            when(response.err.code) {
                0 -> {
                    Log.d(TAG, "Internet Error")
                    when (attempt) {
                        in 0 .. 2 -> {
                            isExistNickname(nickname, attempt + 1)
                        }
                        else -> {
                            true
                        }
                    }
                }
                else -> {
                    Log.d(TAG, "Error code = ${response.err.code}, message = ${response.err.message}")
                    true
                }
            }
        }
    }

}