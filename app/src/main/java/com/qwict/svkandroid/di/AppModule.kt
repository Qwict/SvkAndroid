package com.qwict.svkandroid.di

import com.qwict.svkandroid.SvkAndroidApplication
import com.qwict.svkandroid.common.Constants.BASE_URL
import com.qwict.svkandroid.data.local.LocalDataContainerImpl
import com.qwict.svkandroid.data.remote.ApiService
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
    fun provideApi(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(
        api: ApiService,
    ): SvkRepository {
        return SvkRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideAppContainer(): LocalDataContainerImpl {
        return LocalDataContainerImpl(
            SvkAndroidApplication.appContext,
            CoroutineScope(SupervisorJob() + Dispatchers.Main),
        )
    }
}
