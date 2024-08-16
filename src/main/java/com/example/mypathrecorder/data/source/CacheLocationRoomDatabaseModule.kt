package com.example.mypathrecorder.data.source

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheLocationRoomDatabaseModule {

    @Provides
    @Singleton
    fun provideCacheLocationRoomDatabase(@ApplicationContext context: Context):CacheLocationRoomDatabase{
        return Room.databaseBuilder(
            context,
            CacheLocationRoomDatabase::class.java,
            "cacheLocationDatabase"
        ).build()
    }
}