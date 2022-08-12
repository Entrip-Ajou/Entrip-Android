package ajou.paran.entrip.di

import ajou.paran.entrip.model.WebSocketMessage
import ajou.paran.entrip.screen.planner.top.PlannerActivityViewModel
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import java.net.SocketException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {
    private val url = "ws://2ntrip.com/ws/websocket"
    private val intervalMills = 5000
    private const val TAG = "[WebSocket]"

    @Provides
    @Singleton
    fun provideWebsocket():StompClient{

        val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

        stompClient.withServerHeartbeat(intervalMills).withClientHeartbeat(intervalMills)

        stompClient.lifecycle().subscribe{ lifecycleEvent ->
            when(lifecycleEvent.type){
                LifecycleEvent.Type.OPENED -> {
                    Log.i(TAG, "OPEND!!")
                }
                LifecycleEvent.Type.CLOSED -> {
                    Log.i(TAG, "CLOSED!!")
                }
                LifecycleEvent.Type.ERROR -> {
                    Log.i(TAG, "ERROR!!")
                    Log.e(TAG, "CONNECT ERROR "+lifecycleEvent.exception.toString())

                    when(lifecycleEvent.exception.cause){
                        is SocketException -> {
                            Log.e(TAG, "Socket Exception")
                        }
                    }
                }

                LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                    Log.i(TAG, "FAILED_SERVER_HEARTBEAT")
                }

                else ->{
                    Log.i(TAG, "ELSE "+lifecycleEvent.message)
                }
            }
        }

        return stompClient
    }
}