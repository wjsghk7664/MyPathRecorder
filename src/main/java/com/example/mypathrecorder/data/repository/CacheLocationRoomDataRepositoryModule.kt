package com.example.mypathrecorder.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CacheLocationRoomDataRepositoryModule {
    @Binds
    abstract fun bindCacheLocationRoomDataRepository(cacheLocationRoomDataRepositoryImpl: CacheLocationRoomDataRepositoryImpl):CacheLocationRoomDataRepository
}