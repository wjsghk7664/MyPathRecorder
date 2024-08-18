package com.example.mypathrecorder.domain

import com.example.mypathrecorder.data.repository.CacheLocationRoomDataRepository
import javax.inject.Inject

class ClearCacheMovementDataUseCase @Inject constructor(private val cacheLocationRoomDataRepository: CacheLocationRoomDataRepository) {
    operator suspend fun invoke():Boolean{
        return cacheLocationRoomDataRepository.deleteLocationData()
    }
}