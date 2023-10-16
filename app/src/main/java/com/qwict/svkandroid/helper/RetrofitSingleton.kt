package com.qwict.svkandroid.helper

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitSingleton {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://IP_OFF_YOUR_LAPOT/v1/")
        .addConverterFactory(GsonConverterFactory.create())
//        TODO: We need to add an interceptor... (to add token to header of each request)
//        So that we don't have to propdirll it form the viewModel each time...
//        .client(OkHttpClient.Builder()
//            .addInterceptor(authInterceptor)
//            .build())
        .build()

    fun getRetro(): Retrofit {
        return retrofit
    }
}


