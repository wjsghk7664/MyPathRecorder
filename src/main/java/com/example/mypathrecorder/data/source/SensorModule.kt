package com.example.mypathrecorder.data.source

import android.content.Context
import android.hardware.SensorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ServiceComponent::class)
object SensorModule {

    @Provides
    fun provideStepSensor(@ApplicationContext context: Context): SensorManager {
        return context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
}