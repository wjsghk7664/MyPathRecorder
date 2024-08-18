package com.example.mypathrecorder.data.repository

import com.example.mypathrecorder.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseUserDataRepositoryImpl @Inject constructor(private val firestore:FirebaseFirestore):FirebaseUserDataRepository{
    override fun CheckDupId(id:String, callback:(Boolean,Int) -> Unit){
        firestore.collection("User").document(id).get().addOnSuccessListener { document->
            if(document.exists()){
                callback(false,0)
            }else{
                callback(true,0)
            }
        }.addOnFailureListener { _->
            callback(false,1)
        }
    }

    override fun AddOrModifyUserData(user: User, callback: (Boolean) -> Unit) {
        firestore.collection("User").document(user.id).set(user).addOnSuccessListener {
            callback(true)
        }.addOnFailureListener { _->
            callback(false)
        }
    }

    override fun DeleteUserData(id: String, callback: (Boolean) -> Unit) {
        firestore.collection("User").document(id).delete().addOnSuccessListener {
            callback(true)
        }.addOnFailureListener { _->
            callback(false)
        }
    }

    override fun Login(id: String, password: String, callback: (User?) -> Unit) {
        firestore.collection("User").document(id).get().addOnSuccessListener { document->
            if(document.exists()){
                val user = document.toObject(User::class.java)
                if(user!!.password ==password){
                    callback(user)
                }else{
                    callback(null)
                }
            }else{
                callback(null)
            }
        }.addOnFailureListener { _->
            callback(null)
        }
    }

}