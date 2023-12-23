package com.qwict.svkandroid

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class ImageTest {
    private val testScope = StandardTestDispatcher()
    private val fakeExpenseRepository = FakeSvkRepository()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testScope)
    }

//
//    @Test
//    fun `test viewModel get5Images`() = runTest {
//        assertTrue(viewModel.listUiState is ListUiState.Loading)
//        viewModel.get5Expenses()
//        delay(1000)
//        assertTrue(viewModel.listUiState is ListUiState.Success)
//    }
//
//    @Test
//    fun `test viewModel getExpenses`() = runTest {
//        assertTrue(viewModel.mapUiState is MapUiState.Loading)
//        viewModel.getExpenses(true)
//        delay(1000)
//        assertTrue(viewModel.mapUiState is MapUiState.Success)
//    }

}