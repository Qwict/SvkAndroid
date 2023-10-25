package com.qwict.svkandroid.utils

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.qwict.svkandroid.SvkAndroidApplication
import com.qwict.svkandroid.ui.MainViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            MainViewModel()
        }
    }
}

fun CreationExtras.svkApplication(): SvkAndroidApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SvkAndroidApplication)
