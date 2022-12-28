package ajou.paran.entrip.screen.community.board

import ajou.paran.entrip.model.Comment
import ajou.paran.entrip.repository.Impl.CommunityRepositoryImpl
import ajou.paran.entrip.repository.network.dto.community.ResponseComment
import ajou.paran.entrip.repository.network.dto.community.ResponseFindByIdPhoto
import ajou.paran.entrip.repository.network.dto.community.ResponseNestedComment
import ajou.paran.entrip.repository.network.dto.community.ResponsePost
import ajou.paran.entrip.util.SingleLiveEvent
import ajou.paran.entrip.util.network.BaseResult
import android.content.SharedPreferences
import android.util.Log
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

    private val _post: MutableLiveData<ResponsePost> = MutableLiveData()
    val post: LiveData<ResponsePost>
        get() = _post

    private val _commentList: SingleLiveEvent<List<ResponseComment>> = SingleLiveEvent()
    val commentList: LiveData<List<ResponseComment>>
        get() = _commentList

    private val _isSuccessNestedComment: SingleLiveEvent<Boolean> = SingleLiveEvent()
    val isSuccessNestedComment: LiveData<Boolean>
        get() = _isSuccessNestedComment

    private val _postComment: SingleLiveEvent<Long> = SingleLiveEvent()
    val postComment: LiveData<Long>
        get() = _postComment

    private val _postNestedComment: SingleLiveEvent<Long> = SingleLiveEvent()
    val postNestedComment: LiveData<Long>
        get() = _postNestedComment

    private val userId: String
        get() = sharedPreferences.getString("user_id", "") ?: ""

    val testCommentList: MutableList<Comment> = mutableListOf()

    fun loadPostData(postId: Long) {
        _commentList.call()
        testCommentList.clear()
        CoroutineScope(Dispatchers.IO).launch {
            findPost(postId).join()
            findComment(postId).join()
        }
    }

    fun postComment(postId: Long, content: String) = CoroutineScope(Dispatchers.IO).launch {
        when (val result = communityRepositoryImpl.saveComment(
                author = userId,
                content = content,
                postId = postId
            )
        ) {
            is BaseResult.Success -> {
                _postComment.postValue(result.data)
            }
            is BaseResult.Error -> {
                _postComment.postValue(-1L)
            }
        }

    }

    fun loadNestedComment(list: List<Comment>) = CoroutineScope(Dispatchers.IO).launch {
        list.forEach {
            when (val result = communityRepositoryImpl.getAllNestedCommentsWithPostCommentId(it.comment.commentId)) {
                is BaseResult.Success -> {
                    it.listNestedComment.addAll(result.data)
                }
                is BaseResult.Error -> {
                    Log.d(TAG, "Empty Data")
                }
            }
        }
        _isSuccessNestedComment.postValue(true)
    }

    fun postNestedComment(commentId: Long, content: String) = CoroutineScope(Dispatchers.IO).launch {
        when (val result = communityRepositoryImpl.saveNestedComment(
                author = userId,
                content = content,
                commentId = commentId
            )
        ) {
            is BaseResult.Success -> {
                _postNestedComment.postValue(result.data)
            }
            is BaseResult.Error -> {
                _postNestedComment.postValue(-1L)
            }
        }
    }

    private fun findPost(postId: Long) = CoroutineScope(Dispatchers.IO).launch {
        when (val result = communityRepositoryImpl.findByIdPost(postId)) {
            is BaseResult.Success -> {
                _post.postValue(result.data)
            }
            is BaseResult.Error -> {
                _post.postValue(ResponsePost(-1L))
            }
        }

    }

    private fun findComment(postId: Long) = CoroutineScope(Dispatchers.IO).launch {
        when (val result = communityRepositoryImpl.getAllCommentsWithPostId(postId)) {
            is BaseResult.Success -> {
                _commentList.postValue(result.data)
            }
            is BaseResult.Error -> {
                _commentList.postValue(listOf())
            }
        }
    }
}