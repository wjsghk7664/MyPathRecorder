package com.example.mypathrecorder.domain

import androidx.activity.OnBackPressedCallback
import com.example.mypathrecorder.data.model.User
import com.example.mypathrecorder.data.repository.FirebaseUserDataRepository
import javax.inject.Inject

class RegisterOrModifyUserDataUseCase @Inject constructor(private val firebaseUserDataRepository: FirebaseUserDataRepository) {
    operator fun invoke(user: User, callback: (Boolean)->Unit){
        firebaseUserDataRepository.AddOrModifyUserData(user){
            callback(it)
        }
    }
}