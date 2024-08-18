package com.example.mypathrecorder.domain

import android.graphics.Bitmap
import com.example.mypathrecorder.data.repository.FirebaseImageRepository
import javax.inject.Inject

class UploadImageUsecase @Inject constructor(private val firebaseImageRepository: FirebaseImageRepository) {
    operator fun invoke(bitmap: Bitmap,id:String, compress:Int, callback:(String?)->Unit){
        firebaseImageRepository.uploadImageCompressed(bitmap,id,compress){ bool, url ->
            if(bool){
                callback(url)
            }else{
                callback(null)
            }
        }
    }
}