package com.example.mypathrecorder.data.repository

import com.example.mypathrecorder.data.model.CacheLocation

interface CacheLocationRoomDataRepository {
    suspend fun getAllLocationData():Result<List<CacheLocation>>
    suspend fun getTimeLocationData(upper:Long):Result<List<CacheLocation>>
    suspend fun getRangeLocationData(latR:Long,latU:Long,lonR:Long,lonU:Long):Result<List<CacheLocation>>
    suspend fun deleteLocationData(): Boolean
    suspend fun getCheckedLocationData():Result<List<CacheLocation>>

}