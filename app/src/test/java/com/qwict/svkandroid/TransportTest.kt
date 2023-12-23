package com.qwict.svkandroid

import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.domain.model.Transport
import com.qwict.svkandroid.domain.use_cases.DeleteActiveTransportUseCase
import com.qwict.svkandroid.domain.use_cases.FinishTransportUseCase
import com.qwict.svkandroid.domain.use_cases.GetActiveTransportUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TransportTest {
    private val testScope = StandardTestDispatcher()
    private val fakeSvkRepository = FakeSvkRepository()
    private val resourceProvider = FakeResourceProvider()
    private lateinit var finishTransportUseCase: FinishTransportUseCase
    private lateinit var getActiveTransportUseCase: GetActiveTransportUseCase
    private lateinit var deleteActiveTransportUseCase: DeleteActiveTransportUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testScope)
        finishTransportUseCase = FinishTransportUseCase(fakeSvkRepository, resourceProvider)
        getActiveTransportUseCase = GetActiveTransportUseCase(fakeSvkRepository, resourceProvider)
        deleteActiveTransportUseCase =
            DeleteActiveTransportUseCase(fakeSvkRepository, resourceProvider)
    }

    @Test
    fun `FinishTransportUseCase should return Resource Success with valid transport`() {
        runTest {
            val validTransport = Transport(
                routeNumber = "123",
                driverName = "Test",
                licensePlate = "A-TEST-123",
                images = emptyList(),
                cargos = emptyList(),
            )

            finishTransportUseCase(validTransport).onEach { result ->
                Assert.assertTrue(result is Resource.Success<*>)
            }
        }
    }

    @Test
    fun `FinishTransportUseCase should return Resource Error with invalid transport`() {
        runTest {
            val invalidTransport = Transport(routeNumber = "")
            finishTransportUseCase(invalidTransport).onEach { result ->
                Assert.assertTrue(result is Resource.Error)
            }
        }
    }

    @Test
    fun `getActiveTransportUseCase should return Resource Success`() {
        runTest {
            getActiveTransportUseCase().onEach { result ->
                Assert.assertTrue(result is Resource.Success<*>)
            }
        }
    }

    @Test
    fun `deleteActiveTransportUseCase should return Resource Success`() {
        runTest {
            deleteActiveTransportUseCase().onEach { result ->
                Assert.assertTrue(result is Resource.Success<*>)
            }
        }
    }

    @Test
    fun `finishTransportUseCase should return Resource Error`() {
        runTest {
            finishTransportUseCase(Transport("")).onEach { result ->
                Assert.assertTrue(result is Resource.Error<*>)
            }
        }
    }
}