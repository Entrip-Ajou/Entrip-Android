package ajou.paran.entrip.repository.network

import ajou.paran.entrip.repository.network.api.VoteApi
import ajou.paran.entrip.repository.network.dto.*
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import ajou.paran.entrip.util.network.networkinterceptor.NoInternetException
import android.util.Log
import retrofit2.HttpException
import retrofit2.http.Body
import javax.inject.Inject

class VoteRemoteSource
@Inject
constructor(
    private val voteApi: VoteApi
) {
    companion object {
        const val TAG = "[VoteRemote]"
    }

    suspend fun saveVote(votesSaveRequest: VotesSaveRequestDto) : BaseResult<VoteResponse, Failure>
            = try {
        val res = voteApi.saveVote(votesSaveRequest)
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

    suspend fun updateVote(votesUpdateRequestDto: VotesUpdateRequestDto) : BaseResult<VoteResponse, Failure>
            = try {
        val res = voteApi.updateVotes(votesUpdateRequestDto)
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

    suspend fun deleteVote(vote_id : Long) : BaseResult<Long, Failure>
            = try {
        val res = voteApi.deleteVote(vote_id)
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

    suspend fun doVote(
        votesContentsCountRequestDto: VotesContentsCountRequestDto
    ) : BaseResult<VotesFullInfoReturnDto, Failure>
            = try {
        val res = voteApi.doVote(votesContentsCountRequestDto)
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

    suspend fun getVotes(
        vote_id : Long
    ) : BaseResult<VotesFullInfoReturnDto, Failure>
            = try {
        val res = voteApi.getVotes(vote_id)
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

    suspend fun terminateVote(
        vote_id : Long
    ) : BaseResult<Long, Failure>
            = try {
        val res = voteApi.terminateVote(vote_id)
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

    suspend fun fetchVotes(
        planner_id: Long
    ) : BaseResult<List<VoteResponse>, Failure>
            = try {
        val res = voteApi.fetchVotes(planner_id)
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

    suspend fun getPreviousVotes(
        previousVotesContentsRequestDto: PreviousVotesContentsRequestDto
    ) : BaseResult<List<Long>, Failure>
            = try {
        val res = voteApi.getPreviousVotes(previousVotesContentsRequestDto)
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

    suspend fun undoVotes(
        votesContentsCountRequestDto: VotesContentsCountRequestDto
    ) : BaseResult<VotesFullInfoReturnDto, Failure>
            = try {
        val res = voteApi.undoVotes(votesContentsCountRequestDto)
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