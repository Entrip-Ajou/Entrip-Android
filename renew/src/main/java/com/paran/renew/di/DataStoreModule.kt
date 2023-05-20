package com.paran.renew.di

import ajou.paran.data.local.datasource.DataStoreDataSource
import ajou.paran.data.local.datasourceimpl.DataStoreDataSourceImpl
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideDataStoreDataSource(
        @ApplicationContext context: Context
    ): DataStoreDataSource = DataStoreDataSourceImpl(
        context = context
    )

}