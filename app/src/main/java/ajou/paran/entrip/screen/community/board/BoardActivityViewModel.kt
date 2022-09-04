package ajou.paran.entrip.screen.community.board

import ajou.paran.entrip.repository.Impl.CommunityRepositoryImpl
import ajou.paran.entrip.repository.network.dto.community.ResponseFindByIdPhoto
import ajou.paran.entrip.repository.network.dto.community.ResponsePost
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private val _photoList: MutableLiveData<List<ResponseFindByIdPhoto>> = MutableLiveData()

    val post: LiveData<ResponsePost>
        get() = _post
    val photoList: LiveData<List<ResponseFindByIdPhoto>>
        get() = _photoList

    fun loadPostData(postId: Long) = CoroutineScope(Dispatchers.IO).launch {
        val post = communityRepositoryImpl.findByIdPost(postId)
        _post.postValue(post)
    }

    fun findPhotos(list: List<Long>) = CoroutineScope(Dispatchers.IO).launch {
        val mutableList: MutableList<ResponseFindByIdPhoto> = mutableListOf()
        for (photoId in list.sorted()){
            val photo = communityRepositoryImpl.findByIdPhoto(photoId = photoId)
            mutableList.add(photo)
        }
        _photoList.postValue(mutableList.toList())
    }
}