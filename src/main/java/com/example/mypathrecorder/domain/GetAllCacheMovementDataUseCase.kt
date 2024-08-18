package com.example.mypathrecorder.domain

import com.example.mypathrecorder.data.model.CacheLocation
import com.example.mypathrecorder.data.repository.CacheLocationRoomDataRepository
import javax.inject.Inject

class GetAllCacheMovementDataUseCase @Inject constructor(private val cacheLocationRoomDataRepository: CacheLocationRoomDataRepository){

    operator suspend fun invoke():List<CacheLocation>?{
        return cacheLocationRoomDataRepository.getAllLocationData().getOrNull()
    }
}