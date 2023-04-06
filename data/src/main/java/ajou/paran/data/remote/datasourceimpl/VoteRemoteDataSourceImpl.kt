package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.remote.api.VoteAPI
import ajou.paran.data.remote.datasource.VoteRemoteDataSource
import javax.inject.Inject

class VoteRemoteDataSourceImpl
@Inject
constructor(
    private val voteAPI: VoteAPI,
) : VoteRemoteDataSource {

    override suspend fun saveVote(
        title: String,
        contents: MutableList<String>,
        multipleVotes: Boolean,
        anonymousVotes: Boolean,
        deadLine: String?,
        plannerId: Long,
        author: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateVotes(
        voteId: Long,
        title: String,
        multipleVote: Boolean,
        anonymousVote: Boolean,
        deadLine: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteVoteById(voteId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun doVote(votesId: Long, voteContentIds: MutableList<Long>, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getVotesById(voteId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun terminateVoteById(voteId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchVotesByPlannerId(plannerId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getPreviousVotes(userId: String, voteId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun undoVotes(
        votesId: Long,
        voteContentIds: MutableList<Long>,
        userId: String
    ) {
        TODO("Not yet implemented")
    }

}