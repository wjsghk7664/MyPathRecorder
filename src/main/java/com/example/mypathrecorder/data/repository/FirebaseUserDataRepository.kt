package com.example.mypathrecorder.data.repository

import com.example.mypathrecorder.data.model.User

interface FirebaseUserDataRepository {
    fun CheckDupId(id:String, callback:(Boolean,Int) ->Unit)
    fun AddOrModifyUserData(user: User, callback: (Boolean) -> Unit)
    fun DeleteUserData(id:String, callback: (Boolean) -> Unit)
    fun Login(id:String, password:String, callback: (User?) -> Unit)
}