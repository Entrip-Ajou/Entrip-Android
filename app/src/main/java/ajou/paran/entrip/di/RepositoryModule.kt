package ajou.paran.entrip.di

import ajou.paran.entrip.repository.Impl.PlannerRepositoryImpl
import ajou.paran.entrip.repository.Impl.PlannerRepository
import ajou.paran.entrip.repository.room.plan.dao.PlanDao
import ajou.paran.entrip.repository.Impl.PlanRepository
import ajou.paran.entrip.repository.Impl.PlanRepositoryImpl
import ajou.paran.entrip.repository.network.PlanRemoteSource
import ajou.paran.entrip.repository.network.PlannerRemoteSource
import ajou.paran.entrip.repository.network.api.PlanApi
import ajou.paran.entrip.repository.room.AppDatabase
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
    fun providePlanRemoteSource(planApi: PlanApi) : PlanRemoteSource{
        return PlanRemoteSource(planApi)
    }

    @Provides
    @Singleton
    fun providePlannerRemoteSource(planApi: PlanApi) : PlannerRemoteSource{
        return PlannerRemoteSource(planApi)
    }

    @Provides
    @Singleton
    fun providePlanDao(appDatabase: AppDatabase) : PlanDao {
        return appDatabase.planDao()
    }

    @Provides
    @Singleton
    fun providePlanRepository(planRemoteSource: PlanRemoteSource, planDao: PlanDao) : PlanRepository{
        return PlanRepositoryImpl(planRemoteSource, planDao)
    }

    @Provides
    @Singleton
    fun providePlannerRepository(plannerRemoteSource: PlannerRemoteSource, planDao: PlanDao) : PlannerRepository{
        return PlannerRepositoryImpl(plannerRemoteSource, planDao)
    }
}