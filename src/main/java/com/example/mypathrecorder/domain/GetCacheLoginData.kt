package com.example.mypathrecorder.domain

import com.example.mypathrecorder.data.repository.CacheLoginRepository
import javax.inject.Inject

class GetCacheLoginData @Inject constructor(private val cacheLoginRepository: CacheLoginRepository) {
    operator fun invoke():Pair<String?,String?>?{
        return cacheLoginRepository.getLoginData().getOrNull()
    }
}