package com.example.mypathrecorder.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache_location_table")
data class CacheLocation(
    @PrimaryKey
    val time:Long,
    val lat: Double,
    val lon: Double,
    val step:Int, //이걸로 걷는중인지 이동수단을 타고 있는지 체크(걸음수 변화를 체크)
    val marker: String? = null,
    val img:String? = null
)