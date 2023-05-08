package com.paran.renew.di

import ajou.paran.data.local.datasource.LocalUserDataSource
import ajou.paran.data.local.datasource.PlannerRoomDataSource
import ajou.paran.data.local.datasourceimpl.LocalUserDataSourceImpl
import ajou.paran.data.local.datasourceimpl.PlannerRoomDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Singleton
    @Binds
    abstract fun provideLocalUserDataSource(
        localUserDataSourceImpl: LocalUserDataSourceImpl
    ): LocalUserDataSource

    @Singleton
    @Binds
    abstract fun providePlannerRoomDataSource(
        plannerRoomDataSourceImpl: PlannerRoomDataSourceImpl
    ): PlannerRoomDataSource

}