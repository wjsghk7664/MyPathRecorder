package com.example.mypathrecorder.presentation.Login

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypathrecorder.data.model.User
import com.example.mypathrecorder.domain.CheckSignUpUseCase
import com.example.mypathrecorder.domain.RegisterOrModifyUserDataUseCase
import com.example.mypathrecorder.domain.UploadImageUsecase
import com.example.mypathrecorder.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val checkSignUpUseCase: CheckSignUpUseCase,
    private val registerOrModifyUserDataUseCase: RegisterOrModifyUserDataUseCase,
    private val uploadImageUsecase: UploadImageUsecase
):ViewModel() {

    private val _SignUpUiState = MutableStateFlow<UiState<Unit>>(UiState.Init)
    val SignUpUiState = _SignUpUiState.asStateFlow()

    fun signUp(id:String, password:String, name:String, profile: Bitmap?){
        viewModelScope.launch {
            _SignUpUiState.value = UiState.Loading
        }
        checkSignUpUseCase(id,password,name){notify,isEnable ->
            if(isEnable){
                var user = User(id,password,name,null)
                if(profile!=null){
                    uploadImageUsecase(profile,id,100){url->
                        if(url==null){
                            _SignUpUiState.value = UiState.Failure(notify+"F 이미지 업로드에 실패하였습니다. 다시 시도해주세요.")
                        }else{
                            registerOrModifyUserDataUseCase(user.copy(img=url)){
                                if(it){
                                    _SignUpUiState.value = UiState.Success(Unit)
                                }else{
                                    _SignUpUiState.value = UiState.Failure(notify+"F 등록에 실패하였습니다. 다시 시도해주세요.")
                                }
                            }
                        }
                    }
                }else{
                    registerOrModifyUserDataUseCase(user){
                        if(it){
                            _SignUpUiState.value = UiState.Success(Unit)
                        }else{
                            _SignUpUiState.value = UiState.Failure(notify+"F 등록에 실패하였습니다. 다시 시도해주세요.")
                        }
                    }
                }
            }
            else{
                _SignUpUiState.value = UiState.Failure(notify+"F")
            }
        }
    }
}