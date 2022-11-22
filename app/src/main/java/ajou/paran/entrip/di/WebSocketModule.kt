package ajou.paran.entrip.di

//import ajou.paran.entrip.screen.planner.top.PlannerActivityViewModel
import ajou.paran.entrip.util.network.networkinterceptor.NetworkInterceptor
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {
    private val url = "wss://2ntrip.com/ws/websocket"
    private val intervalMills = 5000
    private const val TAG = "[WebSocket]"

    @Provides
    @Singleton
    fun provideWebSocket(networkInterceptor : NetworkInterceptor):StompClient{
        val client = OkHttpClient.Builder().apply {
            readTimeout(10, TimeUnit.SECONDS)
            connectTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            addInterceptor(networkInterceptor)
        }.build()

        val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url, null, client)
        stompClient.withServerHeartbeat(intervalMills).withClientHeartbeat(intervalMills)

//        stompClient.lifecycle().subscribe{ lifecycleEvent ->
//            when(lifecycleEvent.type){
//                LifecycleEvent.Type.OPENED -> {
//                    Log.i(TAG, "OPEND!!")
//                }
//                LifecycleEvent.Type.CLOSED -> {
//                    Log.i(TAG, "CLOSED!!")
//                    CoroutineScope(Dispatchers.Default).launch{
//                        while(true){
//                            if(networkInterceptor.isConnected()) {
//                                //stompClient.connect()
//                                break;
//                            }else delay(1000)
//
//                        }
//                    }
//                }
//                LifecycleEvent.Type.ERROR -> {
//                    Log.i(TAG, "ERROR!!")
//                    Log.e(TAG, "CONNECT ERROR "+lifecycleEvent.exception.toString())
//                }
//
//                LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
//                    Log.i(TAG, "FAILED_SERVER_HEARTBEAT")
//                }
//
//                else ->{
//                    Log.i(TAG, "ELSE "+lifecycleEvent.message)
//                }
//            }
//        }

        return stompClient
    }

//    @Synchronized fun reconnectSocket(stompClient: StompClient){
//        stompClient.connect()
//    }
}