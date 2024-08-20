package com.example.mypathrecorder.domain

import com.example.mypathrecorder.data.model.CacheLocation
import com.example.mypathrecorder.data.repository.MovementDataRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class CachingMovementDataUseCase @Inject constructor(private val movementDataRepository: MovementDataRepository) {

    private val _cacheData = MutableSharedFlow<CacheLocation>()
    val cacheData = _cacheData.asSharedFlow()

    private var _step = 0
    val step get() = _step

    suspend operator fun invoke():Unit = coroutineScope{
        launch {
            movementDataRepository.getStepFlow().collectLatest {
                _step = it
            }
        }
        launch {
            movementDataRepository.getLocationFlow().collectLatest {
                _cacheData.emit(
                    CacheLocation(
                        System.currentTimeMillis(),
                        it.latitude,
                        it.longitude,
                        step
                    )
                )
            }
        }

    }

}