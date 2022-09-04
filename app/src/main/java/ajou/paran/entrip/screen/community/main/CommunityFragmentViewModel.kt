package ajou.paran.entrip.screen.community.main

import ajou.paran.entrip.repository.Impl.CommunityRepositoryImpl
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
class CommunityFragmentViewModel
@Inject
constructor(
    private val sharedPreferences: SharedPreferences,
    private val communityRepositoryImpl: CommunityRepositoryImpl
): ViewModel() {
    companion object{
        const val TAG = "[CommunityFragmentViewModel]"
    }

    val userId: String
    get() = sharedPreferences.getString("user_id", null) ?: ""

    private var pageNum: Long = 1L

    private val _boardItemList: MutableLiveData<List<ResponsePost>> = MutableLiveData()

    val boardItemList: LiveData<List<ResponsePost>>
    get() = _boardItemList

    fun getNextPage() = CoroutineScope(Dispatchers.IO).launch {
        val list = communityRepositoryImpl.getPostsListWithPageNum(pageNum++)

        if (list.isEmpty()) pageNum--

        _boardItemList.postValue(list)
    }

}