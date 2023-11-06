package com.qwict.svkandroid.ui.viewModels

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
    val state: StateFlow<TransportUiState> = _state.asStateFlow()

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
