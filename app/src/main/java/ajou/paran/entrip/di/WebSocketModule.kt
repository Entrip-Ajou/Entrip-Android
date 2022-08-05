package ajou.paran.entrip.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient

@Module
@InstallIn(ViewModelComponent::class)
object WebSocketModule {
    private val url = "ws://2ntrip.com/ws/websocket"

    @Provides
    @ViewModelScoped
    fun provideWebsocket():StompClient{
        val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)
        return stompClient
    }
}