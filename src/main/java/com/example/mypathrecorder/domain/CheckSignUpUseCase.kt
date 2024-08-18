package com.example.mypathrecorder.domain

import androidx.activity.OnBackPressedCallback
import com.example.mypathrecorder.data.repository.FirebaseUserDataRepositoryImpl
import javax.inject.Inject

class CheckSignUpUseCase @Inject constructor(private val firebaseUserDataRepositoryImpl: FirebaseUserDataRepositoryImpl){
    operator fun invoke(id:String, password:String, name:String, callback: (String,Boolean)->Unit){
        firebaseUserDataRepositoryImpl.CheckDupId(id){ isEnable, type ->
            var resultFlag=true
            var result="아이디: "

            result+=if(id.isEmpty()){
                resultFlag=false
                "빈 칸을 채워주세요."
            }
            else if(isEnable){
                "사용 가능한 아이디입니다."
            }else{
                if(type==0){
                    resultFlag=false
                    "이미 사용중인 아이디입니다."
                }else{
                    resultFlag=false
                    "오류로 인해 확인하지 못하였습니다. 다시 시도해주세요."
                }
            }

            result+="\n비밀번호: "

            val regex= Regex("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[\\W_]).{8,}$")
            result+=if(password.isEmpty()){
                resultFlag=false
                "빈 칸을 채워주세요."
            }
            else if(regex.matches(password)){
                "사용 가능한 비밀번호입니다."
            }else{
                resultFlag=false
                "영문과 숫자, 특수문자를 하나 이상 넣어서 8자리 이상으로 입력하여 주세요."
            }

            if(name.isEmpty()){
                result+="\n이름: 빈 칸을 채워주세요."
                resultFlag=false
            }
            callback(result,resultFlag)
        }
    }
}