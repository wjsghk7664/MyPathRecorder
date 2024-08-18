package com.example.mypathrecorder.presentation.cropimage

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mypathrecorder.R
import com.example.mypathrecorder.databinding.ActivityCropImageBinding
import com.example.mypathrecorder.presentation.Login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class CropImageActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCropImageBinding.inflate(layoutInflater)
    }

    private lateinit var uri:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        uri = intent.getStringExtra("uri")?:""

        initView()
    }

    private fun initView() = with(binding){
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("체크",uri)
            cropCiv.loadImageFromUri(uri)
        }

        val btnList = listOf(cropBtnFree,cropBtn11,cropBtn34,cropBtn43,cropBtn916,cropBtn169,cropBtnFull)
        btnList[0].setTextColor(getColor(R.color.button))
        btnList.forEachIndexed { idx, btn ->
            btn.setOnClickListener{
                btnList.forEach { it.setTextColor(getColor(R.color.white)) }
                if(idx==6){
                    btnList[0].setTextColor(getColor(R.color.button))
                }else{
                    btn.setTextColor(getColor(R.color.button))
                }

                cropCiv.ratio = Ratio.entries.find { it.idx == idx }?:Ratio.RatioFree
                cropCiv.fixRatio()
            }
        }

        cropBtnCancel.setOnClickListener {
            finish()
        }
        cropBtnCrop.setOnClickListener {
            val croppedBitmap = cropCiv.CropBitmap()?.getOrNull()
            CoroutineScope(Dispatchers.IO).launch {
                CropImageEventBus.emitCroppedImg(croppedBitmap)
            }.invokeOnCompletion {
                finish()
            }
        }
    }

}