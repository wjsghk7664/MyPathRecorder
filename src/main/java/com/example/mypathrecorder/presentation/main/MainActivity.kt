package com.example.mypathrecorder.presentation.main

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mypathrecorder.R
import com.example.mypathrecorder.data.source.LocationService
import com.example.mypathrecorder.data.source.StepService
import com.example.mypathrecorder.databinding.ActivityMainBinding
import com.example.mypathrecorder.databinding.DialogFinishRecordingBinding
import com.example.mypathrecorder.presentation.CachingService

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val intents by lazy { listOf(
        Intent(this, CachingService::class.java),
        Intent(this, StepService::class.java),
        Intent(this, LocationService::class.java)
    ) }

    private val fragments = listOf(
        MapFragment.newInstance(),
        RecordListFragment.newInstance(),
        MyPageFragment.newInstance(),
        SettingFragment.newInstance()
    )

    private var isActive = false

    private var backgroundPermission =false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
    }

    private fun initView() = with(binding) {
        val menu = listOf(mainNavMap, mainNavRecord, mainNavMypage, mainNavSetting)
        val color = getColor(R.color.button)
        menu[0].setColorFilter(color)
        supportFragmentManager.beginTransaction()
            .replace(binding.mainClFragmentContainer.id, fragments[0])



        menu.forEachIndexed { idx, btn ->
            btn.setOnClickListener {
                menu.forEach { it.setColorFilter(null) }
                btn.setColorFilter(color)
                supportFragmentManager.beginTransaction()
                    .replace(binding.mainClFragmentContainer.id, fragments[idx])
            }
        }

        mainNavStart.setOnClickListener {
            if(!checkPermission()||!backgroundPermission) {
                return@setOnClickListener
            }
            mainNavStart.isSelected = !mainNavStart.isSelected
            if(mainNavStart.isSelected){
                activateService()
            }else{
                stopService()
            }
            if (!isActive) Toast.makeText(
                this@MainActivity,
                "길게 누르면 기록을 종료할 수 있습니다.",
                Toast.LENGTH_SHORT
            ).show()
            isActive = true
            mainNavStart.setBackgroundResource(R.drawable.button_circle)
        }

        mainNavStart.setOnLongClickListener {
            if (isActive) {
                val builer = AlertDialog.Builder(this@MainActivity)
                val dialogBinding =
                    DialogFinishRecordingBinding.inflate(layoutInflater, null, false)
                builer.setView(dialogBinding.root)
                val dialog = builer.create()
                dialogBinding.finishBtnFinish.setOnClickListener {
                    stopService()
                    mainNavStart.setBackgroundResource(R.drawable.white_circle)
                    mainNavStart.isSelected = false
                    isActive = false
                    dialog.dismiss()
                }
                dialogBinding.finishBtnCancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }
            return@setOnLongClickListener true
        }
    }

    private fun activateService() {
        intents.forEach { startService(it) }
    }

    private fun stopService(){
        intents.forEach { stopService(it) }
    }

    private fun checkPermission():Boolean{
        var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) permissions+= Manifest.permission.ACTIVITY_RECOGNITION
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) permissions +=Manifest.permission.POST_NOTIFICATIONS
        var checkFlag = false

        permissions.forEach { if(checkSelfPermission(it) ==PackageManager.PERMISSION_DENIED) checkFlag=true }

        requestPermissions(permissions,0)
        if(checkFlag) {
            return false
        }else{
            return true
        }
    }

    private fun checkBackgroundPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return
        var permission = arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        if(checkSelfPermission(permission[0]) ==PackageManager.PERMISSION_DENIED) {
            Log.d("권한체크", "백그라운드")
            requestPermissions(permission, 1)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 0){
            if(checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)==PackageManager.PERMISSION_DENIED){
                backgroundPermission=false
                checkBackgroundPermission()
            }

        }else if(requestCode == 1){
            if(checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)==PackageManager.PERMISSION_DENIED){
                backgroundPermission=false
            }else{
                backgroundPermission=true
            }
        }
    }

}