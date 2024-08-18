package com.example.mypathrecorder.data.repository

import android.graphics.Bitmap
import androidx.activity.OnBackPressedCallback

interface FirebaseImageRepository {
    fun uploadImageCompressed(bitmap: Bitmap, id: String, compress:Int, callback: (Boolean, String?) -> Unit)
    fun deleteImage(id:String, callback: (Boolean)->Unit)
}