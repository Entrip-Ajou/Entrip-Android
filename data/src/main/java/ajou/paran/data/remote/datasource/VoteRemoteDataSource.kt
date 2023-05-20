package ajou.paran.data.remote.datasource

interface VoteRemoteDataSource {

    suspend fun saveVote(
        title: String,
        contents: MutableList<String>,
        multipleVotes: Boolean,
        anonymousVotes: Boolean,
        deadLine: String?,
        plannerId: Long,
        author: String,
    )

    suspend fun updateVotes(
        voteId: Long,
        title: String,
        multipleVote: Boolean,
        anonymousVote: Boolean,
        deadLine: String,
    )

    suspend fun deleteVoteById(
        voteId: Long,
    )

    suspend fun doVote(
        votesId: Long,
        voteContentIds: MutableList<Long>,
        userId: String,
    )

    suspend fun getVotesById(
        voteId: Long,
    )

    suspend fun terminateVoteById(
        voteId: Long,
    )

    suspend fun fetchVotesByPlannerId(
        plannerId: Long,
    )

    suspend fun getPreviousVotes(
        userId: String,
        voteId: Long,
    )

    suspend fun undoVotes(
        votesId: Long,
        voteContentIds: MutableList<Long>,
        userId: String,
    )

}