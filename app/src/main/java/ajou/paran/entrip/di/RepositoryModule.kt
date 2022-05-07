package ajou.paran.entrip.di

import ajou.paran.entrip.repository.Impl.UserRepository
import ajou.paran.entrip.repository.Impl.UserRepositoryImpl
import ajou.paran.entrip.repository.network.UserRemoteSource
import ajou.paran.entrip.repository.network.api.PlanApi
import ajou.paran.entrip.repository.network.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePlanRemoteApi(retrofit: Retrofit) : PlanApi {
        return retrofit.create(PlanApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRemoteApi(retrofit: Retrofit) : UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRemoteSource(userApi: UserApi) : UserRemoteSource{
        return UserRemoteSource(userApi)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userRemoteSource: UserRemoteSource) : UserRepository{
        return UserRepositoryImpl(userRemoteSource)
    }
}