package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.VoteRemoteSource
import ajou.paran.entrip.repository.network.dto.*
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import javax.inject.Inject

class VoteRepositoryImpl
@Inject
constructor(
    private val voteRemoteSource: VoteRemoteSource
) : VoteRepository {

    override suspend fun saveVote(votesSaveRequest: VotesSaveRequestDto): BaseResult<VoteResponse, Failure> {
        val res = voteRemoteSource.saveVote(votesSaveRequest)

        when(res){
            is BaseResult.Success -> return BaseResult.Success(res.data)
            is BaseResult.Error -> return BaseResult.Error(Failure(res.err.code, res.err.message))
        }
    }

    override suspend fun updateVote(votesUpdateRequestDto: VotesUpdateRequestDto): BaseResult<VoteResponse, Failure> {
        val res = voteRemoteSource.updateVote(votesUpdateRequestDto)

        when(res){
            is BaseResult.Success -> return BaseResult.Success(res.data)
            is BaseResult.Error -> return BaseResult.Error(Failure(res.err.code, res.err.message))
        }
    }

    override suspend fun deleteVote(vote_id: Long): BaseResult<Long, Failure> {
        val res = voteRemoteSource.deleteVote(vote_id)

        when(res){
            is BaseResult.Success -> return BaseResult.Success(res.data)
            is BaseResult.Error -> return BaseResult.Error(Failure(res.err.code, res.err.message))
        }
    }

    override suspend fun doVote(votesContentsCountRequestDto: VotesContentsCountRequestDto): BaseResult<VotesFullInfoReturnDto, Failure> {
        val res = voteRemoteSource.doVote(votesContentsCountRequestDto)

        when(res){
            is BaseResult.Success -> return BaseResult.Success(res.data)
            is BaseResult.Error -> return BaseResult.Error(Failure(res.err.code, res.err.message))
        }
    }

    override suspend fun getVotes(vote_id: Long): BaseResult<VotesFullInfoReturnDto, Failure> {
        val res = voteRemoteSource.getVotes(vote_id)

        when(res){
            is BaseResult.Success -> return BaseResult.Success(res.data)
            is BaseResult.Error -> return BaseResult.Error(Failure(res.err.code, res.err.message))
        }
    }

    override suspend fun terminateVote(vote_id: Long): BaseResult<Long, Failure> {
        val res = voteRemoteSource.terminateVote(vote_id)

        when(res){
            is BaseResult.Success -> return BaseResult.Success(res.data)
            is BaseResult.Error -> return BaseResult.Error(Failure(res.err.code, res.err.message))
        }
    }

    override suspend fun fetchVotes(planner_id: Long): BaseResult<List<VoteResponse>, Failure> {
        val res = voteRemoteSource.fetchVotes(planner_id)

        when(res){
            is BaseResult.Success -> return BaseResult.Success(res.data)
            is BaseResult.Error -> return BaseResult.Error(Failure(res.err.code, res.err.message))
        }
    }

    override suspend fun getPreviousVotes(
        previousVotesContentsRequestDto: PreviousVotesContentsRequestDto
    ) : BaseResult<List<Long>, Failure> {
        val res = voteRemoteSource.getPreviousVotes(previousVotesContentsRequestDto)

        when(res){
            is BaseResult.Success -> return BaseResult.Success(res.data)
            is BaseResult.Error -> return BaseResult.Error(Failure(res.err.code, res.err.message))
        }
    }

    override suspend fun undoVotes(
        votesContentsCountRequestDto: VotesContentsCountRequestDto
    ) : BaseResult<VotesFullInfoReturnDto, Failure> {
        val res = voteRemoteSource.undoVotes(votesContentsCountRequestDto)

        when(res){
            is BaseResult.Success -> return BaseResult.Success(res.data)
            is BaseResult.Error -> return BaseResult.Error(Failure(res.err.code, res.err.message))
        }
    }
}