package ajou.paran.entrip.screen.community

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommunityFragmentViewModel
@Inject
constructor(
    private val sharedPreferences: SharedPreferences
): ViewModel() {
    companion object{
        const val TAG = "[CommunityFragmentViewModel]"
    }

    fun getUserId(): String? = sharedPreferences.getString("user_id", null)
}