package ajou.paran.entrip.screen.recommendation

import ajou.paran.entrip.model.test.nullRecommenItem
import ajou.paran.entrip.repository.network.dto.TripResponse
import ajou.paran.entrip.repository.usecase.GetUserUseCase
import ajou.paran.entrip.util.network.BaseResult
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommenFragViewModel
@Inject
constructor(
    val sharedPreferences: SharedPreferences,
    private val getUserUseCase: GetUserUseCase
)
: ViewModel() {
    companion object{
        const val TAG = "[RcommenFragVM]"
    }

    val userId: String
    get() = sharedPreferences.getString("user_id", null) ?: ""

    private val _recommendItemList: MutableLiveData<List<TripResponse>> = MutableLiveData()

    val recommendItemList: LiveData<List<TripResponse>>
    get() = _recommendItemList

    fun findByUserId() = viewModelScope.launch {
        getUserUseCase.execute(userId)
            .collect{
                when(it) {
                    is BaseResult.Success -> { _recommendItemList.postValue(it.data) }
                    is BaseResult.Error -> { _recommendItemList.postValue(nullRecommenItem) }
                }
            }
    }
}