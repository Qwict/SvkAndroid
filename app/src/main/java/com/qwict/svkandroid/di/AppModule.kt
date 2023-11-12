package com.qwict.svkandroid.di

import com.qwict.svkandroid.SvkAndroidApplication
import com.qwict.svkandroid.common.Constants.BASE_URL
import com.qwict.svkandroid.data.local.RoomContainer
import com.qwict.svkandroid.data.local.RoomContainerImpl
import com.qwict.svkandroid.data.remote.RetrofitApiService
import com.qwict.svkandroid.data.repository.SvkRepository
import com.qwict.svkandroid.data.repository.SvkRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitApiService(): RetrofitApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRoomContainer(): RoomContainer {
        return RoomContainerImpl(
            SvkAndroidApplication.appContext,
            CoroutineScope(SupervisorJob() + Dispatchers.Main),
        )
    }

    @Provides
    @Singleton
    fun provideRepository(
        api: RetrofitApiService,
        roomContainer: RoomContainer,
    ): SvkRepository {
        return SvkRepositoryImpl(api, roomContainer)
    }

//    @Provides
//    @Singleton
//    fun provideAuthenticationSingleton(): AuthenticationSingleton {
//        return AuthenticationSingleton()
//    }
}
