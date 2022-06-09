package ajou.paran.entrip.screen.recommendation

import ajou.paran.entrip.model.test.nullRecommenItem
import ajou.paran.entrip.repository.Impl.RecommendRepository
import ajou.paran.entrip.repository.network.dto.TripResponse
import ajou.paran.entrip.repository.usecase.GetUserUseCase
import ajou.paran.entrip.util.network.BaseResult
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
class RecommenFragViewModel
@Inject
constructor(
    private val getUserUseCase: GetUserUseCase
)
: ViewModel() {
    companion object{
        const val TAG = "[RcommenFragVM]"
    }

    private val _recommendItemList: MutableLiveData<List<TripResponse>> = MutableLiveData()

    val recommendItemList: LiveData<List<TripResponse>>
        get() = _recommendItemList

    fun findByUserId(user_id: String) = viewModelScope.launch {
        getUserUseCase
            .execute(user_id)
            .collect{
                if (it is BaseResult.Success){
                    _recommendItemList.postValue(it.data)
                } else {
                    _recommendItemList.postValue(nullRecommenItem)
                }
            }
    }
}