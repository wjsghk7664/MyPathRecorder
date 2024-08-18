package com.example.mypathrecorder.data.repository

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class FirebaseImageRepositoryImpl @Inject constructor(private val storage: FirebaseStorage):FirebaseImageRepository {
    override fun uploadImageCompressed(bitmap: Bitmap, id: String, compress:Int, callback: (Boolean, String?) -> Unit) {
        val storageRef = storage.reference.child("image").child(id)

        val baos =ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, compress, baos)
        val data = baos.toByteArray()
        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnSuccessListener { _->
            storageRef.downloadUrl.addOnSuccessListener {
                callback(true,it.toString())
            }.addOnFailureListener {
                callback(false,it.message)
            }
        }.addOnFailureListener{
            callback(false, it.message)
        }
    }

    override fun deleteImage(id: String, callback: (Boolean) -> Unit) {
        storage.reference.child("image").child(id).delete().addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }
}