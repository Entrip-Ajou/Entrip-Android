package ajou.paran.entrip.di

import ajou.paran.entrip.R
import ajou.paran.entrip.base.BaseUrl
import ajou.paran.entrip.repository.network.api.FcmApi.Companion.FCM_URL
import ajou.paran.entrip.repository.network.api.MapApi.Companion.Kakao_URL
import ajou.paran.entrip.repository.network.api.PlanApi
import ajou.paran.entrip.util.EntripV1
import ajou.paran.entrip.util.EntripV2
import ajou.paran.entrip.util.FCM
import ajou.paran.entrip.util.KakaoMap
import ajou.paran.entrip.util.network.auth.AuthInterceptor
import ajou.paran.entrip.util.network.fcm.FcmInterceptor
import ajou.paran.entrip.util.network.kakao.KakaoInterceptor
import ajou.paran.entrip.util.network.networkinterceptor.NetworkInterceptor
import android.content.Context
import android.content.SharedPreferences
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
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @EntripV1
    fun provideRetrofit(
        @EntripV1 client: OkHttpClient
    ) : Retrofit = Retrofit.Builder()
        .apply {
            baseUrl(BaseUrl.MAIN_URL)
            addConverterFactory(GsonConverterFactory.create())
            client(client)
        }
        .build()


    @Provides
    @Singleton
    @EntripV2
    fun provideV2Retrofit(
        @EntripV2 client: OkHttpClient
    ) : Retrofit = Retrofit.Builder().apply {
        baseUrl(BaseUrl.MAIN_URL)
        addConverterFactory(GsonConverterFactory.create())
        client(client)
    }.build()

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
    @KakaoMap
    fun provideKakaoRetrofit(@KakaoMap client : OkHttpClient) : Retrofit{
        return Retrofit.Builder().apply{
            baseUrl(Kakao_URL)
            addConverterFactory(GsonConverterFactory.create())
            client(client)
        }.build()
    }

    @Provides
    @Singleton
    @EntripV1
    fun provideHttpClient(
        networkInterceptor: NetworkInterceptor,
        authInterceptor: AuthInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) : OkHttpClient = OkHttpClient.Builder()
        .apply {
            readTimeout(10, TimeUnit.SECONDS)
            connectTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            addInterceptor(networkInterceptor)
            addInterceptor(authInterceptor)
            addInterceptor(httpLoggingInterceptor)
        }
        .build()


    @Provides
    @Singleton
    @EntripV2
    fun provideV2HttpClient(
        networkInterceptor: NetworkInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) : OkHttpClient = OkHttpClient.Builder().apply {
        readTimeout(10, TimeUnit.SECONDS)
        connectTimeout(10, TimeUnit.SECONDS)
        writeTimeout(10, TimeUnit.SECONDS)
        addInterceptor(networkInterceptor)
        addInterceptor(httpLoggingInterceptor)
    }.build()

    @Provides
    @Singleton
    @FCM
    fun provideFCMHttpClient(
        fcmInterceptor : FcmInterceptor,
        networkInterceptor: NetworkInterceptor
    ) : OkHttpClient {
        return OkHttpClient.Builder().apply{
            readTimeout(10, TimeUnit.SECONDS)
            connectTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            addInterceptor(networkInterceptor)
            addInterceptor(fcmInterceptor)
        }.build()
    }

    @Provides
    @Singleton
    @KakaoMap
    fun provideKakaoHttpClient(
        kakaoInterceptor: KakaoInterceptor,
        networkInterceptor: NetworkInterceptor
    ) : OkHttpClient{
        return OkHttpClient.Builder().apply{
            readTimeout(10, TimeUnit.SECONDS)
            connectTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            addInterceptor(networkInterceptor)
            addInterceptor(kakaoInterceptor)
        }.build()
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
    fun provideKakaoInterceptor() : KakaoInterceptor {
        return KakaoInterceptor()
    }

    @Provides
    fun provideAuthInterceptor(sharedPreferences: SharedPreferences) : AuthInterceptor = AuthInterceptor(sharedPreferences)

    @Provides
    fun provideHttpLoggingInterceptor() : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
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