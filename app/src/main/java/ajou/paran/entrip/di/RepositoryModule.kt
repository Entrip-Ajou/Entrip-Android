package ajou.paran.entrip.di

import ajou.paran.entrip.repository.room.plan.repository.PlanRepository
import ajou.paran.entrip.repository.room.plan.repository.PlanRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindPlanRepository(
        planRepository:PlanRepositoryImpl
    ): PlanRepository
}