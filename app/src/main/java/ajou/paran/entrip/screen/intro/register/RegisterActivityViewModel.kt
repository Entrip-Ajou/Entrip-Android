package ajou.paran.entrip.screen.intro.register

import ajou.paran.entrip.repository.usecase.IsExistNicknameUseCase
import ajou.paran.entrip.util.network.BaseResult
import ajou.paran.entrip.util.network.Failure
import android.content.SharedPreferences
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterActivityViewModel
@Inject
constructor(
    private val sharedPreferences: SharedPreferences,
    private val isExistNicknameUseCase: IsExistNicknameUseCase
): ViewModel() {
    companion object{
        const val TAG = "[RegisterActivityViewModel]"
    }

    private lateinit var userNickname: String

    private val _isExistNicknameResult: MutableLiveData<BaseResult<Boolean, Failure>> = MutableLiveData()

    private val isExistNicknameResult: LiveData<BaseResult<Boolean, Failure>>
        get() = _isExistNicknameResult

    fun setIsExistNickname(nickname: String) = viewModelScope.launch{
        userNickname = nickname
        _isExistNicknameResult.value = isExistNicknameUseCase
            .execute(userNickname)
            .asLiveData()
            .value
    }

    fun getIsExistNickname() = isExistNicknameResult

    fun getUserId() = sharedPreferences.getString("user_id", "")

}