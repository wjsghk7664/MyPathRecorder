package com.example.mypathrecorder.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id:String="",
    val password:String="",
    val name:String="",
    val img:String?=null
):Parcelable