package com.paran.renew.di

import ajou.paran.data.remote.datasource.UserRemoteDataSource
import ajou.paran.data.remote.datasourceimpl.UserRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Singleton
    @Binds
    abstract fun providerUserDataSource(
        remoteUserDataSourceImpl: UserRemoteDataSourceImpl
    ): UserRemoteDataSource

}