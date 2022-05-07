package ajou.paran.entrip.screen.intro.register

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterActivityViewModel
@Inject
constructor(
    private val sharedPreferences: SharedPreferences
): ViewModel() {
    companion object{
        const val TAG = "[RegisterActivtyViewModel]"
    }

    fun getUserId() = sharedPreferences.getString("user_id", "")

}