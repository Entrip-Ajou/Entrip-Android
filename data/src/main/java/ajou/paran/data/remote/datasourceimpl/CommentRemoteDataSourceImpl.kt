package ajou.paran.data.remote.datasourceimpl

import ajou.paran.data.remote.api.CommentAPI
import ajou.paran.data.remote.datasource.CommentRemoteDataSource
import ajou.paran.data.utils.baseApiCall
import javax.inject.Inject

class CommentRemoteDataSourceImpl
@Inject
constructor(
    private val commentAPI: CommentAPI,
) : CommentRemoteDataSource {

    override suspend fun insertComment(
        userId: String,
        content: String,
        planId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCommentById(
        commentId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

    override suspend fun selectCommentByPlanId(
        planId: Long
    ): Unit = baseApiCall {
        TODO("Not yet implemented")
    }

}