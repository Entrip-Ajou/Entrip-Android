package ajou.paran.entrip.screen.trip

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TripTestActivityViewModel
@Inject
constructor(
    private val sharedPreferences: SharedPreferences
)
: ViewModel(){

    val userId: String
    get() = sharedPreferences.getString("user_id", null) ?: ""


}