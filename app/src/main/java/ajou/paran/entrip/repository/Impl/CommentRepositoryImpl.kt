package ajou.paran.entrip.repository.Impl

import ajou.paran.entrip.repository.network.CommentRemoteSource
import ajou.paran.entrip.repository.network.dto.CommentPlanResponse
import ajou.paran.entrip.repository.network.dto.CommentRequest
import ajou.paran.entrip.repository.network.dto.CommentResponse
import ajou.paran.entrip.repository.room.plan.dao.PlanDao
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentRepositoryImpl
@Inject
constructor(
    private val commentRemoteSource: CommentRemoteSource,
    private val planDao: PlanDao
) : CommentRepository {
    override suspend fun insertComment(
        user_id: String,
        content: String,
        plan_id: Long
    ): BaseResult<List<CommentResponse>, Failure> {
        val request = CommentRequest(
            user_id = user_id,
            content = content,
            plan_id = plan_id
        )
        val res = commentRemoteSource.insertComment(request)
        when(res){
            is BaseResult.Success -> {
                planDao.updateIsExistComments(res.data.planEntity.isExistComments, res.data.planEntity.id)
                return BaseResult.Success(res.data.commentResponse)
            }
            is BaseResult.Error -> {
                return BaseResult.Error(Failure(res.err.code, res.err.message))
            }
        }
    }

    override suspend fun deleteComment(
        comment_id: Long,
        plan_id: Long
    ): BaseResult<List<CommentResponse>, Failure> {
        val res = commentRemoteSource.deleteComment(comment_id)
        when(res){
            is BaseResult.Success -> {
                planDao.updateIsExistComments(res.data.planEntity.isExistComments, res.data.planEntity.id)
                return BaseResult.Success(res.data.commentResponse)
            }
            is BaseResult.Error -> {
                return BaseResult.Error(Failure(res.err.code, res.err.message))
            }
        }
    }

    override suspend fun selectComment(plan_id: Long): BaseResult<List<CommentResponse>,Failure> {
        val res = commentRemoteSource.selectComment(plan_id)
        when(res){
            is BaseResult.Success -> return BaseResult.Success(res.data)
            is BaseResult.Error -> return BaseResult.Error(Failure(res.err.code, res.err.message))
        }
    }

}