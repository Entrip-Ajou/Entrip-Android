package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.repository.usecase.IsExistUserUseCase
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class IntroFragmentViewModel
@Inject
constructor(
    private val sharedPreferences: SharedPreferences,
    isExistUserUseCase: IsExistUserUseCase
): ViewModel()
{
    companion object{
        const val TAG = "[IntroFragmentViewModel]"
    }

    val existUserResult = isExistUserUseCase
        .execute<Boolean>(sharedPreferences.getString("user_id", "")!!)
        .asLiveData()

    fun insertUserId(user_id: String){
        sharedPreferences.edit().putString("user_id", user_id).apply()
    }
}