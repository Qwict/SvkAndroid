package com.qwict.svkandroid.common.di

import android.app.Application
import android.content.Context
import com.qwict.svkandroid.SvkAndroidApplication
import com.qwict.svkandroid.common.AuthInterceptor
import com.qwict.svkandroid.common.Constants.BLOB_URL
import com.qwict.svkandroid.common.Constants.SVK_URL
import com.qwict.svkandroid.common.stringRes.AndroidResourceProvider
import com.qwict.svkandroid.common.stringRes.ResourceProvider
import com.qwict.svkandroid.data.local.RoomContainer
import com.qwict.svkandroid.data.local.RoomContainerImpl
import com.qwict.svkandroid.data.remote.BlobApiService
import com.qwict.svkandroid.data.remote.SvkApiService
import com.qwict.svkandroid.data.repository.SvkRepository
import com.qwict.svkandroid.data.repository.SvkRepositoryImpl
import com.qwict.svkandroid.domain.validator.Validators
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSvkApiService(): SvkApiService {
        return Retrofit.Builder()
            .baseUrl(SVK_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient()) // Add our Okhttp client
            .build()
            .create(SvkApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBlobService(): BlobApiService {
        return Retrofit.Builder()
            .baseUrl(BLOB_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient()) // Add our Okhttp client
            .build()
            .create(BlobApiService::class.java)
    }

    @Provides
    @Singleton
    fun okhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
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
        api: SvkApiService,
        roomContainer: RoomContainer,
    ): SvkRepository {
        return SvkRepositoryImpl(api, roomContainer, SvkAndroidApplication.appContext)
    }

    @Provides
    @Singleton
    fun provideValidators(context: Context): Validators {
        return Validators(context)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideResourceProvider(context: Context): ResourceProvider {
        return AndroidResourceProvider(context)
    }

    // TODO: To make testing work should probably inject authsingleton...
//    @Provides
//    @Singleton
//    fun provideAuthenticationSingleton(): AuthenticationSingleton {
//        return AuthenticationSingleton()
//    }
}
