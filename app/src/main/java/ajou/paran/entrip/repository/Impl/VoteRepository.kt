package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.dto.*
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure

interface VoteRepository {

    suspend fun saveVote(votesSaveRequest: VotesSaveRequestDto) : BaseResult<VoteResponse, Failure>

    suspend fun updateVote(votesUpdateRequestDto: VotesUpdateRequestDto) : BaseResult<VoteResponse, Failure>

    suspend fun deleteVote(vote_id : Long) : BaseResult<Long, Failure>

    suspend fun doVote(
        votesContentsCountRequestDto: VotesContentsCountRequestDto
    ) : BaseResult<VotesFullInfoReturnDto, Failure>

    suspend fun getVotes(
        vote_id : Long
    ) : BaseResult<VotesFullInfoReturnDto, Failure>

    suspend fun terminateVote(
        vote_id : Long
    ) : BaseResult<Long, Failure>

    suspend fun fetchVotes(
        planner_id: Long
    ) : BaseResult<List<VoteResponse>, Failure>

    suspend fun getPreviousVotes(
        previousVotesContentsRequestDto: PreviousVotesContentsRequestDto
    ) : BaseResult<List<Long>, Failure>

    suspend fun undoVotes(
        votesContentsCountRequestDto: VotesContentsCountRequestDto
    ) : BaseResult<VotesFullInfoReturnDto, Failure>
}