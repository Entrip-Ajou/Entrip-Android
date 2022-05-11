package ajou.paran.entrip.screen.intro

import ajou.paran.entrip.repository.usecase.IsExistUserUseCase
import android.content.SharedPreferences
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val isExistUserResult = isExistUserUseCase
        .execute(sharedPreferences.getString("user_id", "")!!)
        .asLiveData()

    fun insertUserId(user_id: String){
        sharedPreferences.edit().putString("user_id", user_id).apply()
    }

    fun sharedUserId() = sharedPreferences.getString("user_id", "")!!
}