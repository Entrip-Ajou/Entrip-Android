package ajou.paran.entrip.screen.community.board

import ajou.paran.entrip.repository.Impl.CommunityRepositoryImpl
import ajou.paran.entrip.repository.network.dto.community.ResponseComment
import ajou.paran.entrip.repository.network.dto.community.ResponseFindByIdPhoto
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

    private val _post: MutableLiveData<ResponsePost> = MutableLiveData()
    private val _commentList: SingleLiveEvent<List<ResponseComment>> = SingleLiveEvent()

    val post: LiveData<ResponsePost>
        get() = _post
    val commentList: LiveData<List<ResponseComment>>
        get() = _commentList

    fun loadPostData(postId: Long) {
        _commentList.call()
        CoroutineScope(Dispatchers.IO).launch {
            findPost(postId).join()
            findComment(postId).join()
        }
    }

    private fun findPost(postId: Long) = CoroutineScope(Dispatchers.IO).launch {
        val post = communityRepositoryImpl.findByIdPost(postId)
        _post.postValue(post)
    }

    private fun findComment(postId: Long) = CoroutineScope(Dispatchers.IO).launch {
        val list = communityRepositoryImpl.getAllCommentsWithPostId(postId)
        _commentList.postValue(list)
    }
}