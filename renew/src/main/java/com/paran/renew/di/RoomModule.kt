package com.paran.renew.di

import ajou.paran.data.db.EntripDatabase
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
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): EntripDatabase = Room.databaseBuilder(
        context,
        EntripDatabase::class.java,
        EntripDatabase.DB_NAME
    ).build()

}