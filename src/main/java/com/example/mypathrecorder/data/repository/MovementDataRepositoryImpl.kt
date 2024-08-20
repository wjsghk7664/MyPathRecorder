package com.example.mypathrecorder.data.repository

import android.location.Location
import com.example.mypathrecorder.data.source.LocationService
import com.example.mypathrecorder.data.source.StepService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class MovementDataRepositoryImpl @Inject constructor(
):MovementDataRepository {

    private val _locationFlow = MutableSharedFlow<Location>()
    val locationFlow = _locationFlow.asSharedFlow()

    private val _stepFlow = MutableSharedFlow<Int>()
    val stepFlow = _stepFlow.asSharedFlow()

    override suspend fun setLocation(location:Location){
        _locationFlow.emit(location)
    }

    override suspend fun setStep(step:Int) {
        _stepFlow.emit(step)
    }

    override fun getLocationFlow(): Flow<Location> {
        return locationFlow
    }

    override fun getStepFlow(): Flow<Int> {
        return stepFlow
    }
}