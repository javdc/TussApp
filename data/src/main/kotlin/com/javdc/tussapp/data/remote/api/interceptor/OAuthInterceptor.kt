package com.javdc.tussapp.data.remote.api.interceptor

import com.javdc.tussapp.data.remote.datasource.TokenDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class OAuthInterceptor(private val tokenDataSource: TokenDataSource): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenDataSource.getAccessToken()

        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        val response = chain.proceed(authenticatedRequest)

        if (response.code == 401) {
            return runBlocking {
                val newToken = tokenDataSource.refreshToken() ?: return@runBlocking response
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()
                return@runBlocking chain.proceed(newRequest)
            }
        }

        return response
    }

}