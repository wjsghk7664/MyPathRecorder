package com.example.mypathrecorder.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mypathrecorder.data.model.CacheLocation
import com.example.mypathrecorder.data.model.LocationData

@Database(entities = [CacheLocation::class], version = 1)
abstract class CacheLocationRoomDatabase: RoomDatabase() {
    abstract fun CacheLocationsDao():CacheLocationsDao
}