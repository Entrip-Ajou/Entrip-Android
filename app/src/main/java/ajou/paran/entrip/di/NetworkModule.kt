package ajou.paran.entrip.di

import ajou.paran.entrip.R
import ajou.paran.entrip.repository.network.api.FcmApi.Companion.FCM_URL
import ajou.paran.entrip.repository.network.api.PlanApi
import ajou.paran.entrip.util.network.fcm.FcmInterceptor
import ajou.paran.entrip.util.network.networkinterceptor.NetworkInterceptor
import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Entrip

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class FCM

    @Provides
    @Singleton
    @Entrip
    fun provideRetrofit(@Entrip client: OkHttpClient) : Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(PlanApi.BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
            client(client)
        }.build()
    }

    @Provides
    @Singleton
    @FCM
    fun provideFCMRetrofit(@FCM client : OkHttpClient) : Retrofit{
        return Retrofit.Builder().apply{
            baseUrl(FCM_URL)
            addConverterFactory(GsonConverterFactory.create())
            client(client)
        }.build()
    }

    @Provides
    @Singleton
    @Entrip
    fun provideHttpClient(networkInterceptor: NetworkInterceptor) : OkHttpClient {
        return OkHttpClient.Builder().apply {
            readTimeout(10, TimeUnit.SECONDS)
            connectTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            addInterceptor(networkInterceptor)
        }.build()
    }

    @Provides
    @Singleton
    @FCM
    fun provideFCMHttpClient(
        fcmInterceptor : FcmInterceptor
    ) : OkHttpClient = OkHttpClient.Builder()
        .run{
            addInterceptor(fcmInterceptor)
            build()
        }

    @Provides
    fun provideInterceptor(@ApplicationContext context: Context) : NetworkInterceptor{
        return NetworkInterceptor(context)
    }

    @Provides
    fun provideFcmInterceptor() : FcmInterceptor{
        return FcmInterceptor()
    }

    @Provides
    @Singleton
    fun provideGoogleSignInClient(@ApplicationContext applicationContext: Context): GoogleSignInClient {
        val mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(applicationContext.getString(R.string.default_web_client_id2))
            .requestProfile()
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(applicationContext, mGoogleSignInOptions)
    }

    @Provides
    @Singleton
    fun provideFirebase(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}