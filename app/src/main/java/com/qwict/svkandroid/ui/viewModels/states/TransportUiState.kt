package com.qwict.svkandroid.ui.viewModels.states

data class TransportUiState(
    val routeNumber: Int = 0,
    val images: List<Int> = emptyList(),

    val isLoading: Boolean = false,
    val error: String = "",
)
