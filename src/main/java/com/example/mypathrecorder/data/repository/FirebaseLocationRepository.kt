package com.example.mypathrecorder.data.repository

import com.example.mypathrecorder.data.model.LocationData
import com.example.mypathrecorder.data.model.LocationList
import com.example.mypathrecorder.data.model.LocationTitleList
import com.google.firebase.firestore.GeoPoint

interface FirebaseLocationRepository {
    fun createTitleList(id:String, callback: (Boolean) -> Unit)
    fun deleteTitleList(id:String, callback: (Boolean) -> Unit)
    fun addLocationTitle(title:Long, id:String, callback: (Boolean) -> Unit)
    fun deleteLocationTitle(title:Long, id:String, callback: (Boolean) -> Unit)
    fun getLocationTitleList(id:String, callback: (LocationTitleList?) -> Unit)
    fun addPathRecord(id:String, locations:HashMap<Long, LocationData>, title:String, callback: (Boolean) -> Unit)
    fun deletePathRecord(id:String, title:String,callback: (Boolean) -> Unit)
    fun getPathRecord(id:String, title:String, callback: (LocationList?) -> Unit)
}