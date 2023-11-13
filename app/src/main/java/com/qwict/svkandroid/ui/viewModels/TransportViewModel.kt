package com.qwict.svkandroid.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.qwict.svkandroid.R
import com.qwict.svkandroid.ui.viewModels.states.TransportUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TransportViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(TransportUiState())
    var showDialogState by mutableStateOf(false)
        private set
    var selectedImage by mutableStateOf(0)
        private set

    val state: StateFlow<TransportUiState> = _state.asStateFlow()

    // TODO change to real data
    var images by mutableStateOf(
        listOf(
            R.drawable.transport_two,
            R.drawable.transport_three,
            R.drawable.transport_four,
        ),
    )

    fun deleteImageOnIndex(imageIndex: Int) {
        Log.i("TEST", "deleteImageOnIndex: $imageIndex")
        if (imageIndex >= 0 && imageIndex < images.size) {
            images = images.toMutableList().apply {
                removeAt(imageIndex)
            }
            Log.i("TEST", "deleteImageOnIndex: $images")
        } else {
            // Handle index out of bounds, e.g., throw an exception or log an error
            println("Invalid index: $imageIndex")
        }
    }

    fun toggleShowDialogState(imageIndex: Int) {
        selectedImage = imageIndex
        showDialogState = !showDialogState
    }

    init {
        _state.value = TransportUiState(
            isLoading = false,
            error = "",
            images = mutableListOf(
                R.drawable.transport_two,
                R.drawable.transport_three,
                R.drawable.transport_four,
            ),
        )
    }
}
