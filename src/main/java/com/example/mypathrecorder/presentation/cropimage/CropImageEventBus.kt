package com.example.mypathrecorder.presentation.cropimage

import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object CropImageEventBus {

    private val _event = MutableSharedFlow<Bitmap?>()
    val event = _event.asSharedFlow()

    suspend fun emitCroppedImg(bitmap: Bitmap?){
        _event.emit(bitmap)
    }
}