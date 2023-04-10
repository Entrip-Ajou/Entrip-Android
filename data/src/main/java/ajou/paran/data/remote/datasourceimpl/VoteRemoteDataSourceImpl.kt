package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.remote.api.VoteAPI
import ajou.paran.data.remote.datasource.VoteRemoteDataSource
import ajou.paran.data.utils.baseApiCall
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
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun updateVotes(
        voteId: Long,
        title: String,
        multipleVote: Boolean,
        anonymousVote: Boolean,
        deadLine: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun deleteVoteById(
        voteId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun doVote(
        votesId: Long,
        voteContentIds: MutableList<Long>,
        userId: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun getVotesById(
        voteId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun terminateVoteById(
        voteId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun fetchVotesByPlannerId(
        plannerId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun getPreviousVotes(
        userId: String,
        voteId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun undoVotes(
        votesId: Long,
        voteContentIds: MutableList<Long>,
        userId: String
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

}