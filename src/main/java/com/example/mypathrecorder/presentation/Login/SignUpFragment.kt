package com.example.mypathrecorder.presentation.Login

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mypathrecorder.R
import com.example.mypathrecorder.databinding.FragmentSignUpBinding
import com.example.mypathrecorder.presentation.UiState
import com.example.mypathrecorder.presentation.cropimage.CropImageActivity
import com.example.mypathrecorder.presentation.cropimage.CropImageEventBus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding:FragmentSignUpBinding? = null
    private val binding get()= _binding!!

    private val viewModel:SignUpViewModel by viewModels()

    private var profile : Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUiState()
        initView()
        getCropImg()
    }

    private fun initView() = with(binding){
        signupIvBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val photoPicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
            if(uri!=null){
                startActivity(Intent(requireActivity(),CropImageActivity::class.java).apply { putExtra("uri",uri.toString()) })
            }
            else{
                profile=null
                signupIvProfile.setImageResource(R.drawable.ic_person)
            }
        }
        signupIvProfile.setOnClickListener {
            photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        signupBtnProfileReset.setOnClickListener {
            profile = null
            signupIvProfile.setImageResource(R.drawable.ic_person)
        }
        signupBtnSignup.setOnClickListener {
            val id = signupEtId.text.toString()
            val password = signupEtPassword.text.toString()
            val name = signupEtName.text.toString()
            viewModel.signUp(id, password, name, profile)
        }
    }

    private fun getCropImg(){
        viewLifecycleOwner.lifecycleScope.launch {
            CropImageEventBus.event.collectLatest {
                it?.let {
                    profile=it
                    binding.signupIvProfile.setImageBitmap(it)
                }
            }
        }
    }

    private fun getUiState() = with(binding){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.SignUpUiState.collectLatest {
                when(it){
                    is UiState.Init -> null
                    is UiState.Loading -> signupProgressBar.visibility=View.VISIBLE
                    is UiState.Success -> {
                        Toast.makeText(requireActivity(), "회원가입 완료", Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                    is UiState.Failure -> {
                        signupProgressBar.visibility=View.GONE
                        val notify=it.e.split("F")
                        if(notify[1].isNotEmpty()) Toast.makeText(requireActivity(), notify[1], Toast.LENGTH_SHORT).show()
                        signupTvNotify.setText(notify[0])
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance() = SignUpFragment()
    }
}