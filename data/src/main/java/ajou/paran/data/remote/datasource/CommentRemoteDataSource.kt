package ajou.paran.data.remote.datasource

interface CommentRemoteDataSource {

    suspend fun insertComment(
        userId: String,
        content: String,
        planId: Long
    )

    suspend fun deleteCommentById(
        commentId: Long,
    )

    suspend fun selectCommentByPlanId(
        planId: Long
    )

}