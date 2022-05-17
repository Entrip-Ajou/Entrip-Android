package ajou.paran.entrip.screen.recommendation

import ajou.paran.entrip.model.test.RecommendationItem
import ajou.paran.entrip.repository.Impl.RecommendRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecommenActViewModel
@Inject
constructor(
    private val recommendRepository: RecommendRepository
)
: ViewModel() {
    companion object{
        const val TAG = "[RcommenFragVM]"
    }

    private val _recommendItemList: MutableLiveData<List<RecommendationItem>> = MutableLiveData()

    val recommendItemList: LiveData<List<RecommendationItem>>
        get() = _recommendItemList

    fun getFakeTestItem() = viewModelScope.launch {
        withContext(IO){
            _recommendItemList.postValue(recommendRepository.getRecommendItem())
        }
    }

}