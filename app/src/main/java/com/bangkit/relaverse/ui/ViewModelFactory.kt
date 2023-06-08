package com.bangkit.relaverse.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.relaverse.data.RelaverseRepository
import com.bangkit.relaverse.data.di.Injection
import com.bangkit.relaverse.ui.auth.AuthViewModel
import com.bangkit.relaverse.ui.create_event.CreateEventViewModel
import com.bangkit.relaverse.ui.main.DetailViewModel
import com.bangkit.relaverse.ui.main.MainViewModel
import com.bangkit.relaverse.ui.main.campaign.ListUserViewModel
import com.bangkit.relaverse.ui.main.joined.JoinedViewModel
import com.bangkit.relaverse.ui.main.profile.ProfileViewModel

class ViewModelFactory private constructor(
    private val repository: RelaverseRepository,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(CreateEventViewModel::class.java)) {
            return CreateEventViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(ListUserViewModel::class.java)) {
            return ListUserViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(JoinedViewModel::class.java)) {
            return JoinedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            context: Context,
        ): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.getRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}