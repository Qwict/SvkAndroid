package com.qwict.svkandroid

import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.domain.use_cases.LoginUseCase
import com.qwict.svkandroid.domain.use_cases.RegisterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthenticationTest {
    private val testScope = StandardTestDispatcher()
    private val fakeSvkRepository = FakeSvkRepository()
    private val resourceProvider = FakeResourceProvider()
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var registerUseCase: RegisterUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testScope)
        loginUseCase = LoginUseCase(fakeSvkRepository, resourceProvider)
        registerUseCase = RegisterUseCase(fakeSvkRepository, resourceProvider)
    }

    @Test
    fun `loginUseCase should return Resource Success`() {
        runTest {
            loginUseCase("test@test.com", "test").onEach { result ->
                assertTrue(result is Resource.Success<*>)
            }
        }
    }

    @Test
    fun `loginUseCase should return Resource Error`() {
        runTest {
            loginUseCase("", "").onEach { result ->
                assertTrue(result is Resource.Error)
            }
        }
    }

    @Test
    fun `registerUseCase should return Resource Success`() {
        runTest {
            registerUseCase("test2@test.com", "test").onEach { result ->
                assertTrue(result is Resource.Success<*>)
            }
        }
    }

    @Test
    fun `registerUseCase should return Resource Error`() {
        runTest {
            registerUseCase("", "").onEach { result ->
                assertTrue(result is Resource.Error)
            }
        }
    }
}
