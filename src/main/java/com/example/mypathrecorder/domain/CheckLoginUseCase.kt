package com.example.mypathrecorder.domain

import androidx.activity.OnBackPressedCallback
import com.example.mypathrecorder.data.model.User
import com.example.mypathrecorder.data.repository.FirebaseUserDataRepository
import javax.inject.Inject

class CheckLoginUseCase @Inject constructor(private val firebaseUserDataRepository: FirebaseUserDataRepository) {
    operator fun invoke(id:String, password:String, callback: (User?)->Unit){
        firebaseUserDataRepository.Login(id,password){
            callback(it)
        }
    }
}