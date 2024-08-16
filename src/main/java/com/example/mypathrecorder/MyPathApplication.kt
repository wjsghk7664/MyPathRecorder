package com.example.mypathrecorder

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyPathApplication:Application() {

    override fun onCreate() {
        super.onCreate()
    }
}