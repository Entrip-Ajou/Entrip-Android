package com.paran.renew.di

import ajou.paran.data.db.EntripDatabase
import ajou.paran.data.db.dao.PlanDao
import ajou.paran.data.db.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun providePlanDao(entripDatabase: EntripDatabase): PlanDao = entripDatabase.planDao()

    @Provides
    @Singleton
    fun provideUserDao(entripDatabase: EntripDatabase): UserDao = entripDatabase.userDao()

}