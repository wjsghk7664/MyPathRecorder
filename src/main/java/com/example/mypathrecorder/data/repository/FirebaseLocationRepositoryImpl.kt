package com.example.mypathrecorder.data.repository

import com.example.mypathrecorder.data.model.LocationData
import com.example.mypathrecorder.data.model.LocationList
import com.example.mypathrecorder.data.model.LocationTitleList
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.SetOptions
import javax.inject.Inject

class FirebaseLocationRepositoryImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore):FirebaseLocationRepository {

    override fun createTitleList(id:String, callback: (Boolean) -> Unit){
        firebaseFirestore.collection("LocationTitleList").document(id).set(LocationTitleList()).addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }

    override fun deleteTitleList(id:String, callback: (Boolean) -> Unit){
        firebaseFirestore.collection("LocationTitleList").document(id).delete().addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }

    override fun addLocationTitle(title:Long, id:String, callback: (Boolean) -> Unit){
        firebaseFirestore.collection("LocationTitleList").document(id).update("locationTitleList",FieldValue.arrayUnion(title))
            .addOnSuccessListener {
                callback(true)
            }.addOnFailureListener {
                callback(false)
            }
    }

    override fun deleteLocationTitle(title:Long, id:String, callback: (Boolean) -> Unit){
        firebaseFirestore.collection("LocationTitleList").document(id).update("locationTitleList", FieldValue.arrayRemove(title))
            .addOnSuccessListener {
                callback(true)
            }.addOnFailureListener {
                callback(true)
            }
    }

    override fun getLocationTitleList(id:String, callback: (LocationTitleList?) -> Unit){
        firebaseFirestore.collection("LocationTitleList").document(id).get().addOnSuccessListener { document ->
            if(document.exists()){
                callback(document.toObject(LocationTitleList::class.java))
            }else{
                callback(null)
            }
        }.addOnFailureListener {
            callback(null)
        }
    }

    override fun addPathRecord(id:String, locations:HashMap<Long, LocationData>, title:String, callback: (Boolean) -> Unit) {
        firebaseFirestore.collection("Location").document(id).collection("Path").document(title)
            .set(LocationList(locations))
            .addOnSuccessListener {
                callback(true)
            }.addOnFailureListener {
                callback(false)
            }
    }

    override fun deletePathRecord(id:String, title:String,callback: (Boolean) -> Unit){
        firebaseFirestore.collection("Location").document(id).collection("Path").document(title).delete()
            .addOnSuccessListener {
                callback(true)
            }.addOnFailureListener {
                callback(false)
            }
    }

    override fun getPathRecord(id:String, title:String, callback: (LocationList?) -> Unit){
        firebaseFirestore.collection("Location").document(id).collection("Path").document(title).get()
            .addOnSuccessListener { document ->
                if(document!=null&&document.exists()){
                    val locations = document.toObject(LocationList::class.java)
                    callback(locations)
                }else{
                    callback(null)
                }
            }.addOnFailureListener {
                callback(null)
            }
    }


}