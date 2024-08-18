package com.example.mypathrecorder.domain

import com.example.mypathrecorder.data.model.CacheLocation
import com.example.mypathrecorder.data.repository.MovementDataRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class CachingMovementDataUseCase @Inject constructor(private val movementDataRepository: MovementDataRepository) {

    private val stepFlow = movementDataRepository.getStep()
    private val locationFlow = movementDataRepository.getLocation()

    private val _cacheData = MutableSharedFlow<CacheLocation>()
    val cacheData = _cacheData.asSharedFlow()

    private var step = 0

    suspend operator fun invoke():Unit = coroutineScope{
        launch {
            stepFlow.collect{
                step = it
            }
        }
        launch {
            locationFlow.collect{
                val cacheLocation = CacheLocation(System.currentTimeMillis(),it.latitude,it.longitude,step)
                _cacheData.emit(cacheLocation)
            }
        }

    }

}