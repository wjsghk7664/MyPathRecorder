package com.example.mypathrecorder.domain

import com.example.mypathrecorder.data.repository.CacheLoginRepository
import javax.inject.Inject

class SaveCacheLoginDataUseCase @Inject constructor(private val cacheLoginRepository: CacheLoginRepository) {
    operator fun invoke(id:String, password:String):Boolean?{
        return cacheLoginRepository.saveLoginData(id,password).getOrNull()

    }
}