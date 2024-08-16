package com.example.mypathrecorder.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.mypathrecorder.data.model.CacheLocation

@Dao
interface CacheLocationsDao {
    @Query("SELECT * FROM cache_location_table ORDER BY time ASC")
    suspend fun getLocations(): List<CacheLocation>

    @Query("SELECT * FROM cache_location_table WHERE marker != null OR img != null ORDER BY time ASC")
    suspend fun getCheckedLocations(): List<CacheLocation>

    //추가 뿐만아니라 사진 링크나 마커 추가시 덮어쓰기 방식으로 바꾸기
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(cacheLocation: CacheLocation)

    @Query("DELETE FROM cache_location_table")
    suspend fun ClearTable()
}