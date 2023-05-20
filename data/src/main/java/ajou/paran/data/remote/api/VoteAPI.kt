package ajou.paran.data.remote.api

import ajou.paran.data.remote.model.request.*
import ajou.paran.data.remote.model.response.*
import retrofit2.http.*

interface VoteAPI {

    @POST("api/v1/votes")
    suspend fun saveVote(
        @Body request: SaveVoteRequest
    ): SaveVoteResponse

    @PUT("api/v1/votes")
    suspend fun updateVotes(
        @Body request: UpdateVotesRequest
    ): UpdateVotesResponse

    @DELETE("api/v1/votes/{vote_id}")
    suspend fun deleteVoteById(
        @Path("vote_id") voteId: Long
    ): Long

    @POST("api/v1/votes/doVote")
    suspend fun doVote(
        @Body request: DoVoteRequest
    ): DoVoteResponse

    @GET("api/v1/votes/{vote_id}")
    suspend fun getVotesById(
        @Path("vote_id") voteId: Long
    ): GetVotesByIdResponse

    @POST("api/v1/votes/{vote_id}")
    suspend fun terminateVoteById(
        @Path("vote_id") voteId : Long
    ): Long

    @GET("api/v1/planners/{planner_id}/allVotes")
    suspend fun fetchVotesByPlannerId(
        @Path("planner_id") plannerId : Long
    ): FindAllVotesByPlannerIdResponse

    @POST("api/v1/votes/getPreviousVotes")
    suspend fun getPreviousVotes(
        @Body request: GetPreviousVotesRequest
    ): List<Long>

    @POST("api/v1/votes/undoVote")
    suspend fun undoVotes(
        @Body request: UndoVotesRequest
    ): UndoVotesResponse

}