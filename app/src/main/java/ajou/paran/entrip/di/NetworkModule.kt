package ajou.paran.entrip.di

import ajou.paran.entrip.repository.network.api.PlanApi
import ajou.paran.entrip.util.network.networkinterceptor.NetworkInterceptor
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient) : Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(PlanApi.BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
            client(client)
        }.build()
    }

    @Provides
    @Singleton
    fun provideHttpClient(networkInterceptor: NetworkInterceptor) : OkHttpClient {
        return OkHttpClient.Builder().apply {
            readTimeout(10, TimeUnit.SECONDS)
            connectTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            addInterceptor(networkInterceptor)
        }.build()
    }

    @Provides
    fun provideInterceptor(@ApplicationContext context: Context) : NetworkInterceptor{
        return NetworkInterceptor(context)
    }
}