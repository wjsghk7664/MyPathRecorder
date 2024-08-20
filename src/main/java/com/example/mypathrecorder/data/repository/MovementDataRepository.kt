package com.example.mypathrecorder.data.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface MovementDataRepository {
    suspend fun setLocation(location:Location)
    suspend fun setStep(step:Int)
    fun getLocationFlow(): Flow<Location>
    fun getStepFlow(): Flow<Int>
}