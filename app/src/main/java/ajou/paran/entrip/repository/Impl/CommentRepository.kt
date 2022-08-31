package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.dto.CommentResponse
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure

interface CommentRepository {
    suspend fun insertComment(user_id : String, content : String, plan_id : Long) : BaseResult<List<CommentResponse>, Failure>
    suspend fun deleteComment(comment_id : Long, plan_id : Long) : BaseResult<List<CommentResponse>, Failure>
    suspend fun selectComment(plan_id : Long) : BaseResult<List<CommentResponse>,Failure>
}