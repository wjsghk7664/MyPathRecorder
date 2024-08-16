package com.example.mypathrecorder.data.repository

import android.location.Location
import com.example.mypathrecorder.data.source.LocationService
import com.example.mypathrecorder.data.source.StepService
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class MovementDataRepositoryImpl @Inject constructor(
    private val locationService: LocationService,
    private val stepService:StepService
):MovementDataRepository {
    override fun getLocation():SharedFlow<Location> {
        return locationService.locationSharedFlow
    }

    override fun getStep():SharedFlow<Int> {
        return stepService.stepSharedFlow
    }


}