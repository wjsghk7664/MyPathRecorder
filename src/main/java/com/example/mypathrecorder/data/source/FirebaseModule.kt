package com.example.mypathrecorder.data.source

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object FirebaseModule {

    @Provides
    fun provideFirestore():FirebaseFirestore{
        return Firebase.firestore
    }

    @Provides
    fun provideFireStorage(): FirebaseStorage{
        return Firebase.storage
    }
}