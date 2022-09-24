package ajou.paran.entrip.screen.community.board

import ajou.paran.entrip.model.Comment
import ajou.paran.entrip.repository.Impl.CommunityRepositoryImpl
import ajou.paran.entrip.repository.network.dto.community.ResponseComment
import ajou.paran.entrip.repository.network.dto.community.ResponseFindByIdPhoto
import ajou.paran.entrip.repository.network.dto.community.ResponseNestedComment
import ajou.paran.entrip.repository.network.dto.community.ResponsePost
import ajou.paran.entrip.util.SingleLiveEvent
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class BoardActivityViewModel
@Inject
constructor(
    private val sharedPreferences: SharedPreferences,
    private val communityRepositoryImpl: CommunityRepositoryImpl
) : ViewModel() {
    companion object {
        private const val TAG = "[BoardActVM]"
    }

    //region private val field
    private val _post: MutableLiveData<ResponsePost> = MutableLiveData()
    private val _commentList: SingleLiveEvent<List<ResponseComment>> = SingleLiveEvent()
    private val _isSuccessNestedComment: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val _postComment: MutableLiveData<Long> = SingleLiveEvent()

    private val userId: String
        get() = sharedPreferences.getString("user_id", "") ?: ""
    //endregion

    //region public val field
    val post: LiveData<ResponsePost>
        get() = _post
    val commentList: LiveData<List<ResponseComment>>
        get() = _commentList
    val isSuccessNestedComment: LiveData<Boolean>
        get() = _isSuccessNestedComment
    val postComment: LiveData<Long>
        get() = _postComment
    val testCommentList: MutableList<Comment> = mutableListOf()
    //endregion

    //region public function
    fun loadPostData(postId: Long) {
        _commentList.call()
        testCommentList.clear()
        CoroutineScope(Dispatchers.IO).launch {
            findPost(postId).join()
            findComment(postId).join()
        }
    }

    fun postComment(postId: Long, content: String) = CoroutineScope(Dispatchers.IO).launch {
        val post = communityRepositoryImpl.saveComment(
            author = userId,
            content = content,
            postId = postId
        )
        _postComment.postValue(post)
    }

    fun loadNestedComment(list: List<Comment>) = CoroutineScope(Dispatchers.IO).launch {
        list.forEach {
            it.listNestedComment.addAll(communityRepositoryImpl.getAllNestedCommentsWithPostCommentId(it.comment.commentId))
        }
        _isSuccessNestedComment.postValue(true)
    }
    //endregion

    //region private function
    private fun findPost(postId: Long) = CoroutineScope(Dispatchers.IO).launch {
        val post = communityRepositoryImpl.findByIdPost(postId)
        _post.postValue(post)
    }

    private fun findComment(postId: Long) = CoroutineScope(Dispatchers.IO).launch {
        val list = communityRepositoryImpl.getAllCommentsWithPostId(postId)
        _commentList.postValue(list)
    }
    //endregion
}