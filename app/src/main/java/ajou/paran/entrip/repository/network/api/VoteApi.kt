package ajou.paran.entrip.repository.network.api

import ajou.paran.entrip.repository.network.dto.*
import retrofit2.http.*

interface VoteApi {
    @POST("api/v1/votes")
    suspend fun saveVote(
        @Body votesSaveRequestDto : VotesSaveRequestDto
    ) : BaseResponse<VoteResponse>

    @PUT("api/v1/votes")
    suspend fun updateVotes(
        @Body votesUpdateRequestDto: VotesUpdateRequestDto
    ) : BaseResponse<VoteResponse>

    @DELETE("api/v1/votes/{vote_id}")
    suspend fun deleteVote(
        @Path("vote_id") vote_id : Long
    ) : BaseResponse<Long>

    @POST("api/v1/votes/doVote")
    suspend fun doVote(
        @Body votesContentsCountRequestDto: VotesContentsCountRequestDto
    ) : BaseResponse<VotesFullInfoReturnDto>

    @GET("api/v1/votes/{vote_id}")
    suspend fun getVotes(
        @Path("vote_id") vote_id : Long
    ) : BaseResponse<VotesFullInfoReturnDto>

    @POST("api/v1/votes/{vote_id}")
    suspend fun terminateVote(
        @Path("vote_id") vote_id : Long
    ) : BaseResponse<Long>

    @GET("api/v1/planners/{planner_id}/allVotes")
    suspend fun fetchVotes(
        @Path("planner_id") planner_id : Long
    ) : BaseResponse<List<VoteResponse>>


    @POST("api/v1/votes/getPreviousVotes")
    suspend fun getPreviousVotes(
        @Body previousVotesContentsRequestDto: PreviousVotesContentsRequestDto
    ) : BaseResponse<List<Long>>

    @POST("api/v1/votes/undoVote")
    suspend fun undoVotes(
        @Body votesContentsCountRequestDto: VotesContentsCountRequestDto
    ) : BaseResponse<VotesFullInfoReturnDto>
}