package com.example.mypathrecorder.data.model

import com.google.firebase.firestore.GeoPoint

data class LocationData(
    val location:GeoPoint? = null,
    val marker:String? = null,
    val img:String? = null
)