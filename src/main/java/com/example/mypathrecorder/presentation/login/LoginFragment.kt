package com.example.mypathrecorder.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mypathrecorder.R
import com.example.mypathrecorder.databinding.FragmentLoginBinding
import com.example.mypathrecorder.presentation.main.MainActivity
import com.example.mypathrecorder.presentation.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel:LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        getUiState()
        viewModel.autoLogin()
    }

    fun getUiState() = with(binding){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userDataUiState.collectLatest {
                when(it){
                    is UiState.Init -> null
                    is UiState.Success -> {
                        Toast.makeText(requireActivity(), "로그인 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(),MainActivity::class.java)
                        intent.putExtra("user",it.data)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    is UiState.Failure ->{
                        loginProgressBar.visibility = View.GONE
                        if(it.e.first() == 'L') Toast.makeText(requireActivity(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Loading -> loginProgressBar.visibility =View.VISIBLE
                }
            }
        }
    }

    fun initView() = with(binding){
        backPressed()

        loginBtnLogin.setOnClickListener {
            val id = loginEtId.text.toString()
            val password = loginEtPassword.text.toString()
            val isAutoLogin = loginCbAutologin.isChecked
            if(id.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireActivity(), "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.CheckLogin(id,password,isAutoLogin)

        }

        loginBtnSignup.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.main,SignUpFragment.newInstance()).addToBackStack(null).commit()
        }
    }

    fun backPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    AlertDialog.Builder(requireContext())
                        .setMessage("정말 이 앱을 종료하시겠습니까?")
                        .setPositiveButton("예") { _, _ ->
                            // 뒤로가기 동작 계속 진행
                            isEnabled = false
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                        .setNegativeButton("아니오", null)
                        .show()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}