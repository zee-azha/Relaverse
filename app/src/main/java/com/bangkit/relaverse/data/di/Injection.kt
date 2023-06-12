package com.bangkit.relaverse.data.di

import android.content.Context
import com.bangkit.relaverse.data.RelaverseRepository
import com.bangkit.relaverse.data.local.UserPreferences
import com.bangkit.relaverse.data.local.dataStore
import com.bangkit.relaverse.data.remote.retrofit.ApiConfig

object Injection {
    fun getRepository(context: Context): RelaverseRepository {
        return RelaverseRepository(
            ApiConfig.getApiService(),
            UserPreferences.getInstance(dataStore = context.dataStore)
        )
    }
}