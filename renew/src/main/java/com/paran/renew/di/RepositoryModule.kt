package com.paran.renew.di

import ajou.paran.data.db.EntripDatabase
import ajou.paran.data.db.dao.PlanDao
import ajou.paran.data.db.dao.UserDao
import ajou.paran.data.repository.PlanRepositoryImpl
import ajou.paran.data.repository.PlannerRepositoryImpl
import ajou.paran.data.repository.UserRepositoryImpl
import ajou.paran.domain.repository.PlanRepository
import ajou.paran.domain.repository.PlannerRepository
import ajou.paran.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Singleton
    @Binds
    abstract fun providePlannerRepository(
        plannerRepositoryImpl: PlannerRepositoryImpl
    ): PlannerRepository

    @Singleton
    @Binds
    abstract fun providePlanRepository(
        planRepositoryImpl: PlanRepositoryImpl
    ): PlanRepository

}