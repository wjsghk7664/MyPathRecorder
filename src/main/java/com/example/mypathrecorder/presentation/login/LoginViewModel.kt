package com.example.mypathrecorder.presentation.login

import androidx.lifecycle.ViewModel
import com.example.mypathrecorder.data.model.User
import com.example.mypathrecorder.domain.CheckLoginUseCase
import com.example.mypathrecorder.domain.GetCacheLoginData
import com.example.mypathrecorder.domain.SaveCacheLoginDataUseCase
import com.example.mypathrecorder.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val checkLoginUseCase: CheckLoginUseCase,
    private val saveCacheLoginDataUseCase: SaveCacheLoginDataUseCase,
    private val getCacheLoginData: GetCacheLoginData
) : ViewModel() {

    private val _userDataUiState = MutableStateFlow<UiState<User>>(UiState.Loading)
    val userDataUiState = _userDataUiState.asStateFlow()

    fun CheckLogin(id: String, password: String, isCache: Boolean) {
        _userDataUiState.value = UiState.Loading
        checkLoginUseCase(id,password){ user ->
            if(user!=null){
                var result = true
                if(isCache){
                    result=saveCacheLoginDataUseCase(id,password)?:false
                }
                if(result){
                    _userDataUiState.value = UiState.Success(user)
                }else{
                    _userDataUiState.value = UiState.Failure("L:SaveLoginDataFail")
                }
            }
            else{
                _userDataUiState.value = UiState.Failure("L:UserDataNotMatch")
            }
        }
    }

    fun autoLogin(){
        val result =getCacheLoginData()
        if(result==null){
            _userDataUiState.value = UiState.Failure("A:NoLoginData")
        }else{
            val id= result.first
            val password = result.second
            if(id!=null&&password!=null){
                CheckLogin(id,password,true)
            }else{
                _userDataUiState.value = UiState.Failure("A:LoadCacheLoginDataFail")
            }
        }
    }
}