package com.qwict.svkandroid.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.domain.use_cases.SelectRouteUseCase
import com.qwict.svkandroid.ui.viewModels.states.TransportUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TransportViewModel @Inject constructor(
    private val selectRouteUseCase: SelectRouteUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(TransportUiState())

    private var routeNumber by mutableStateOf(0)
    var showDialogState by mutableStateOf(false)
        private set
    var selectedImage by mutableStateOf(0)
        private set
    var transportUiState by mutableStateOf(TransportUiState())
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

    fun selectRoute(
        routeNr : Int
    ) {
        routeNumber = routeNr
        Log.i("TransportViewModel", "select route : $routeNr")

        selectRouteUseCase(routeNumber).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    transportUiState = TransportUiState(
                        routeNumber = result.data!!
                    )
                    println("TransportUIState look like this : $transportUiState")
                }

                is Resource.Error -> {
                    transportUiState = TransportUiState(
                        error = result.message ?: "There was an error finding a rout with number: $routeNr"
                    )
                }

                is Resource.Loading -> {
                   transportUiState = transportUiState.copy(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
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
