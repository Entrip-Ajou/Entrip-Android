package ajou.paran.entrip.repository.network

import ajou.paran.entrip.repository.network.api.NoticeApi
import ajou.paran.entrip.repository.network.dto.BaseResponse
import ajou.paran.entrip.repository.network.dto.NoticeResponse
import ajou.paran.entrip.repository.network.dto.NoticesSaveRequest
import ajou.paran.entrip.repository.network.dto.NoticesUpdateRequest
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import android.util.Log
import retrofit2.HttpException
import retrofit2.http.*
import javax.inject.Inject

class NoticeRemoteSource
@Inject
constructor(
    private val noticeApi: NoticeApi
) {
    companion object {
        const val TAG = "[NoticeRemote]"
    }

    suspend fun saveNotice(noticesSaveRequest: NoticesSaveRequest) : BaseResult<NoticeResponse, Failure>
    = try {
        val res = noticeApi.saveNotice(noticesSaveRequest)
        when(res.status){
            200 -> BaseResult.Success(res.data)

            else -> {
                Log.e(TAG, "Err code = ${res.status}, Err message = ${res.message}")
                BaseResult.Error(Failure(res.status, res.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(e.code(), e.message.toString()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun updateNotice(notice_id : Long, noticesUpdateRequest: NoticesUpdateRequest) : BaseResult<NoticeResponse, Failure>
    = try {
        val res = noticeApi.updateNotice(notice_id, noticesUpdateRequest)
        when(res.status){
            200 -> BaseResult.Success(res.data)
            else -> {
                Log.e(TAG, "Err code = ${res.status}, Err message = ${res.message}")
                BaseResult.Error(Failure(res.status, res.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(e.code(), e.message.toString()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun deleteNotice(notice_id : Long) : BaseResult<Long, Failure>
    = try {
        val res = noticeApi.deleteNotice(notice_id)
        when(res.status){
            200 -> BaseResult.Success(res.data)
            else -> {
                Log.e(TAG, "Err code = ${res.status}, Err message = ${res.message}")
                BaseResult.Error(Failure(res.status, res.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(e.code(), e.message.toString()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun fetchAllNotices(planner_id : Long) : BaseResult<List<NoticeResponse>, Failure>
    = try {
        val res = noticeApi.fetchAllNotices(planner_id)
        when(res.status){
            200 -> {
                val notices = mutableListOf<NoticeResponse>()
                res.data?.forEach{ t ->
                    notices.add(
                        NoticeResponse(
                            notice_id = t.notice_id,
                            author = t.author,
                            nickname = t.nickname,
                            title = t.title,
                            content = t.content,
                            modifiedDate = t.modifiedDate
                        )
                    )
                }
                BaseResult.Success(notices)
            }
            else -> {
                Log.e(PlannerRemoteSource.TAG, "Err code = " + res.status + " Err message = " + res.message)
                BaseResult.Error(Failure(res.status, res.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(e.code(), e.message.toString()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(-1, e.message.toString()))
    }

    suspend fun fetchNotice(notice_id : Long) : BaseResult<NoticeResponse, Failure>
    = try {
        val res = noticeApi.fetchNotice(notice_id)
        when(res.status){
            200 -> BaseResult.Success(res.data)
            else -> {
                Log.e(TAG, "Err code = ${res.status}, Err message = ${res.message}")
                BaseResult.Error(Failure(res.status, res.message))
            }
        }
    } catch (e: NoInternetException) {
        Log.e(TAG, "NoInternetException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(0, e.message))
    } catch (e: HttpException) {
        Log.e(TAG, "HttpException Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(e.code(), e.message.toString()))
    } catch (e: Exception) {
        Log.e(TAG, "Exception Message = ${e.localizedMessage}")
        BaseResult.Error(Failure(-1, e.message.toString()))
    }
}