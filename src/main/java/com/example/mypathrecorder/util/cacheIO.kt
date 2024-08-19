package com.example.mypathrecorder.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

fun <T> saveSerializableCache(data:T, name:String, context: Context):Result<String>{
    return runCatching {
        val cacheDir = context.cacheDir
        val file = File(cacheDir,"${name}.cache")
        val fileOutputStream = FileOutputStream(file)
        val objectOutputStream = ObjectOutputStream(fileOutputStream)
        objectOutputStream.writeObject(data)
        objectOutputStream.close()
        fileOutputStream.close()

        Uri.fromFile(file).toString()
    }
}

fun <T> loadSerializableCache(uri:String, context: Context, type: Class<T>):Result<T?>{
    return runCatching {
        when(type){
            Bitmap::class.java ->{
                val inputStream = context.contentResolver.openInputStream(Uri.parse(uri))
                BitmapFactory.decodeStream(inputStream) as T
            }
            else -> null
        }
    }
}

fun saveBitmapCache(bitmap:Bitmap, name:String, context: Context):Result<String>{
    return runCatching {
        val cacheDir = context.cacheDir
        val file = File(cacheDir,"${name}.cache")

        FileOutputStream(file).use{
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        Uri.fromFile(file).toString()
    }
}

fun loadBitmapCache(uri:String, context: Context):Result<Bitmap>{
    return runCatching {
        BitmapFactory.decodeStream(context.contentResolver.openInputStream(Uri.parse(uri)))
    }
}