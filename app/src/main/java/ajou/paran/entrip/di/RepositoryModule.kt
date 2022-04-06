package ajou.paran.entrip.di

import ajou.paran.entrip.repository.Impl.PlannerDumRepositoryImpl
import ajou.paran.entrip.repository.PlannerDumRepository
import ajou.paran.entrip.repository.room.planner.repository.PlannerRepository
import ajou.paran.entrip.repository.room.planner.repository.PlannerRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providePlannerDumRepository(): PlannerDumRepository = PlannerDumRepositoryImpl()
}