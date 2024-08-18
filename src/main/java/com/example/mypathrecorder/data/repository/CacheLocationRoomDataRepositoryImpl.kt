package com.example.mypathrecorder.data.repository

import com.example.mypathrecorder.data.model.CacheLocation
import com.example.mypathrecorder.data.source.CacheLocationRoomDatabase
import javax.inject.Inject

class CacheLocationRoomDataRepositoryImpl @Inject constructor(private val db:CacheLocationRoomDatabase):CacheLocationRoomDataRepository {
    override suspend fun getAllLocationData(): Result<List<CacheLocation>> {
        return runCatching {
            db.CacheLocationsDao().getLocations()
        }
    }

    override suspend fun getTimeLocationData(upper: Long): Result<List<CacheLocation>> {
        return kotlin.runCatching {
            db.CacheLocationsDao().getTimeRangeLocations(upper)
        }
    }

    override suspend fun getRangeLocationData(
        latR: Long,
        latU: Long,
        lonR: Long,
        lonU: Long
    ): Result<List<CacheLocation>> {
        return runCatching {
            db.CacheLocationsDao().getLatLonRangeLocations(latR, latU, lonR, lonU)
        }
    }


    override suspend fun deleteLocationData():Boolean {
        return runCatching{ db.CacheLocationsDao().ClearTable() }.isSuccess
    }

    override suspend fun getCheckedLocationData(): Result<List<CacheLocation>> {
        return runCatching {
            db.CacheLocationsDao().getCheckedLocations()
        }
    }

}