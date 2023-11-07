package com.qwict.svkandroid.ui.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.qwict.svkandroid.common.Constants.EMPTY_USER
import com.qwict.svkandroid.ui.viewModels.states.SvkAndroidUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(SvkAndroidUiState())
    val uiState: StateFlow<SvkAndroidUiState> = _uiState.asStateFlow()

    var user by mutableStateOf(EMPTY_USER)
    val snackbarHostState = SnackbarHostState()

    val currentBarcode = mutableStateOf("")
    val currentQrcode = mutableStateOf("")

    var appJustLaunched by mutableStateOf(true)

    var laadBonnen = mutableListOf<String>()

    private val TAG = "MainViewModel"
    private lateinit var context: Context

    fun setContext(activityContext: Context) {
        context = activityContext
    }

    init {
        Log.d(TAG, "init: MainViewModel")
    }
}
