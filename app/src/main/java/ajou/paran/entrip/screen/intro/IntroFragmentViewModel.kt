package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.repository.usecase.IsExistUserUseCase
import ajou.paran.entrip.util.ApiState
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.google.android.gms.common.api.Api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class IntroFragmentViewModel
@Inject
constructor(
    private val isExistUserUseCase: IsExistUserUseCase,
    private val sharedPreferences: SharedPreferences
): ViewModel()
{
    companion object{
        const val TAG = "[IntroFragmentViewModel]"
    }

    private val _isExistUserResult = MutableStateFlow<ApiState>(ApiState.Init)

    val isExistUserResult: StateFlow<ApiState>
        get() = _isExistUserResult

    lateinit var user_id: String

    fun result(user_id: String) = viewModelScope.launch{
        isExistUserUseCase
            .execute(user_id)
            .collect {
                if (it is BaseResult.Success){
                    if (it.data){
                        _isExistUserResult.value = ApiState.Failure(999)
                    } else {
                        _isExistUserResult.value = ApiState.Success(it)
                    }
                } else {
                    _isExistUserResult.value = ApiState.Failure((it as BaseResult.Error).err.code)
                }
            }
    }

    fun userIdShared(user_id: String) = sharedPreferences.edit().putString("user_id", user_id)
}