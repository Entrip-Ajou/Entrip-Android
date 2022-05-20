package ajou.paran.entrip.util.network.fcm

import ajou.paran.entrip.repository.network.UserRemoteSource
import ajou.paran.entrip.util.network.BaseResult
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessaingService : FirebaseMessagingService() {

    companion object{
        const val TAG = "[FirebaseService]"
    }

    @Inject
    lateinit var sharedPreferences : SharedPreferences

    @Inject
    lateinit var userRemoteSource : UserRemoteSource

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sharedPreferences.edit().putString("token", token).commit()
        CoroutineScope(Dispatchers.IO).launch{
            val user_id = sharedPreferences.getString("user_id", "default").toString()

            if(user_id != "default"){
                val res = userRemoteSource.updateUserToken(token, user_id)
                if(res is BaseResult.Success){
                    Log.d(TAG, "Update the user's token Success")
                }else{
                    Log.e(TAG, "Err code = "+(res as BaseResult.Error).err.code+ " Err message = "+res.err.message)
                }
            }else{
                Log.e(TAG, "앱 초기 다운로드이므로 user_id가 없음")
            }
        }
    }
}