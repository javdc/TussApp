package com.javdc.tussapp.data.remote.api.interceptor

import com.javdc.tussapp.data.remote.datasource.TokenDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class OAuthInterceptor(private val tokenDataSource: TokenDataSource): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // On first launch (or after a failed refresh) there is no stored token.
        // Previously this sent "Authorization: Bearer null", which the API rejects
        // with 400 instead of 401, so the refresh logic never ran.
        val token = tokenDataSource.getAccessToken()
            ?: runBlocking { tokenDataSource.refreshToken() }

        val response = chain.proceed(originalRequest.withToken(token))

        if (response.code == 401) {
            val newToken = runBlocking { tokenDataSource.refreshToken() } ?: return response
            response.close()
            return chain.proceed(originalRequest.withToken(newToken))
        }

        return response
    }

    private fun Request.withToken(token: String?): Request =
        if (token.isNullOrBlank()) this
        else newBuilder().header("Authorization", "Bearer $token").build()

}
