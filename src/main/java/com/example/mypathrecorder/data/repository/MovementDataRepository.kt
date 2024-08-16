package com.example.mypathrecorder.data.repository

import android.location.Location
import kotlinx.coroutines.flow.SharedFlow

interface MovementDataRepository {
    fun getLocation(): SharedFlow<Location>
    fun getStep():SharedFlow<Int>
}