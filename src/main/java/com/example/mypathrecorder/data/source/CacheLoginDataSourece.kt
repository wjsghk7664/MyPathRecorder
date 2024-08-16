package com.example.mypathrecorder.data.source

import android.content.SharedPreferences
import javax.inject.Inject

class CacheLoginDataSourece @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun saveLoginData(id:String, password:String):Boolean{
        return sharedPreferences.edit().putString("id",id).putString("password",password).commit()
    }

    fun getLoginData():Pair<String?,String?>{
        val id =sharedPreferences.getString("id",null)
        val password=sharedPreferences.getString("password",null)
        return Pair(id,password)
    }

    fun deleteLoginData():Boolean{
        return sharedPreferences.edit().clear().commit()
    }
}