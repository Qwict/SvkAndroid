package com.qwict.svkandroid.common

import com.qwict.svkandroid.data.local.getEncryptedPreference
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor class responsible for adding the Authorization header with the user token to the outgoing requests.
 */
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        getEncryptedPreference("token")?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}
