package ajou.paran.entrip.screen.intro.login

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class LoginActivityViewModel
@Inject
constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel()
{
    companion object{
        const val TAG = "[LoginActivtyViewModel]"
    }

    suspend fun isExistUser(user_id: String) = flow<Int> {
        TODO(" API user isExsit Case")
    }

    fun insertUserId(user_id: String){
        sharedPreferences.edit().putString("user_id", user_id).apply()
    }

}