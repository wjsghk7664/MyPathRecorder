package com.example.mypathrecorder.data.repository

interface CacheLoginRepository {
    fun saveLoginData(id:String, password:String):Result<Boolean>
    fun getLoginData():Result<Pair<String?,String?>>
    fun deleteLoginData():Result<Boolean>
}