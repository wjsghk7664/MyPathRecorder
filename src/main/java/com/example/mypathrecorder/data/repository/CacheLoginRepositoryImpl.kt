package com.example.mypathrecorder.data.repository

import com.example.mypathrecorder.data.source.CacheLoginDataSourece
import javax.inject.Inject

class CacheLoginRepositoryImpl @Inject constructor(private val cacheLoginDataSourece: CacheLoginDataSourece):CacheLoginRepository {
    override fun saveLoginData(id: String, password: String): Result<Boolean> {
        return runCatching {
            cacheLoginDataSourece.saveLoginData(id, password)
        }
    }

    override fun getLoginData(): Result<Pair<String?, String?>> {
        return runCatching {
            cacheLoginDataSourece.getLoginData()
        }
    }

    override fun deleteLoginData(): Result<Boolean> {
        return runCatching {
            cacheLoginDataSourece.deleteLoginData()
        }
    }

}