package com.example.mypathrecorder.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MovementDataRepositoryModule {
    @Binds
    abstract fun bindMovementDataRepository(movementDataRepositoryImpl: MovementDataRepositoryImpl):MovementDataRepository
}