package ajou.paran.entrip.di

import ajou.paran.entrip.repository.room.AppDatabase
import ajou.paran.entrip.repository.room.plan.dao.PlanDao
import ajou.paran.entrip.repository.room.plan.repository.PlanRepository
import ajou.paran.entrip.repository.room.plan.repository.PlanRepositoryImpl
import ajou.paran.entrip.repository.room.planner.dao.PlannerDao
import ajou.paran.entrip.repository.room.planner.repository.PlannerRepository
import ajou.paran.entrip.repository.room.planner.repository.PlannerRepositoryImpl
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase{
        return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DB_NAME)
            .build()
    }

    @Provides
    fun providePlanDao(appDatabase: AppDatabase) : PlanDao {
        return appDatabase.planDao()
    }


    @Provides
    fun providePlanRepository(planDao: PlanDao) : PlanRepository{
        return PlanRepositoryImpl(planDao)
    }

    @Provides
    fun providePlannerDao(appDatabase: AppDatabase) : PlannerDao
        = appDatabase.plannerDao()

    @Provides
    fun providePlannerRepository(plannerDao: PlannerDao) : PlannerRepository
        = PlannerRepositoryImpl(plannerDao)
}