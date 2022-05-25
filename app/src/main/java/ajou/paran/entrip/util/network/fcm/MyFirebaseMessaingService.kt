package ajou.paran.entrip.util.network.fcm

import ajou.paran.entrip.R
import ajou.paran.entrip.model.InviteEntity
import ajou.paran.entrip.repository.network.UserRemoteSource
import ajou.paran.entrip.repository.room.plan.dao.UserDao
import ajou.paran.entrip.screen.planner.main.MainActivity
import ajou.paran.entrip.util.network.BaseResult
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

private const val CHANNEL_ID = "Entrip_channel_ID"

@AndroidEntryPoint
class MyFirebaseMessaingService : FirebaseMessagingService() {

    companion object{
        const val TAG = "[FirebaseService]"
    }

    @Inject
    lateinit var sharedPreferences : SharedPreferences

    @Inject
    lateinit var userRemoteSource : UserRemoteSource

    @Inject
    lateinit var userDao: UserDao

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sharedPreferences.edit().putString("token", token).commit()
        CoroutineScope(Dispatchers.IO).launch{
            val user_id = sharedPreferences.getString("user_id", null)?.toString()

            if(!user_id.isNullOrEmpty()){
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

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = Intent(this, MainActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_entrip)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        /**
         *   isInvite = true -> 초대장을 받는 로직
         *   isInvite = false -> 받은 초대장을 응답하는 로직
         */
        if(message.data["isInvite"].toBoolean()){
            CoroutineScope(Dispatchers.IO).launch{
                val inviteEntity = InviteEntity(
                    nickname = message.data["owner"].toString(),
                    photoUrl = message.data["photo_url"].toString(),
                    token = message.data["owner_token"].toString(),
                    planner_title = message.data["planner_title"].toString(),
                    planner_id = message.data["planner_id"].toString().toLong()
                )
                userDao.insertInvite(inviteEntity)
            }
        }else{
            CoroutineScope(Dispatchers.IO).launch{
                userDao.deleteWaiting(message.data["owner_id"].toString(), message.data["planner_id"].toString().toLong())
            }
        }
        notificationManager.notify(notificationID, notification)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager:NotificationManager){
        val channelName = "Entrip_Channel"
        val channel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH).apply{
            description = "Entrip"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }
}