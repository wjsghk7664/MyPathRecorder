package com.example.mypathrecorder.presentation

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.mypathrecorder.data.source.CacheLocationRoomDatabase
import com.example.mypathrecorder.domain.CachingMovementDataUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class CachingService @Inject constructor(private val cachingMovementDataUseCase: CachingMovementDataUseCase, private val cacheLocationRoomDatabase: CacheLocationRoomDatabase) : Service() {

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO +job)

    override fun onCreate() {
        super.onCreate()

        coroutineScope.launch {
            launch {
                cachingMovementDataUseCase()
            }
            launch {
                runCatching {
                    cachingMovementDataUseCase.cacheData.collectLatest {
                        cacheLocationRoomDatabase.CacheLocationsDao().insertLocation(it)
                    }
                }
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}