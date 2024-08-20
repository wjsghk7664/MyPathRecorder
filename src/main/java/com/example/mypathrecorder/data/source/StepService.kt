package com.example.mypathrecorder.data.source

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mypathrecorder.data.repository.MovementDataRepository

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StepService : Service(), SensorEventListener {

    @Inject lateinit var sensorManager: SensorManager
    @Inject lateinit var movementDataRepository: MovementDataRepository

    private var stepCounterSensor: Sensor? =null
    private var initSteps = 0

    override fun onCreate() {
        super.onCreate()

        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        stepCounterSensor?.let {
            sensorManager.registerListener(this,stepCounterSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if(event.sensor.type ==Sensor.TYPE_STEP_COUNTER){
                if(initSteps == 0){
                    initSteps = event.values[0].toInt()
                }
                val steps = event.values[0].toInt() - initSteps
                CoroutineScope(Dispatchers.IO).launch {
                    movementDataRepository.setStep(steps)
                }

            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}