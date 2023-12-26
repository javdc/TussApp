package com.javdc.tussapp.data.remote.datasource

import android.content.SharedPreferences
import com.javdc.tussapp.common.util.tryOrNull
import com.javdc.tussapp.data.remote.api.AuthService
import com.javdc.tussapp.data.remote.model.TokenResponseDto

interface TokenDataSource {
    fun getAccessToken(): String?
    suspend fun refreshToken(): String?
}

class TokenDataSourceImpl(
    private val sharedPreferences: SharedPreferences,
    private val authService: AuthService,
) : TokenDataSource {

    override fun getAccessToken(): String? =
        sharedPreferences.getString(KEY_ACCESS_TOKEN, null)

    override suspend fun refreshToken(): String? {
        val response = tryOrNull("Error getting token from remote data source", "TokenDataSourceImpl") {
            sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
                ?.let { refreshTokenFromRemote(it) }
                ?: getTokenFromRemote()
        }

        sharedPreferences.edit()
            .putString(KEY_ACCESS_TOKEN, response?.accessToken)
            .putString(KEY_REFRESH_TOKEN, response?.refreshToken)
            .apply()

        return response?.accessToken
    }

    private suspend fun getTokenFromRemote(): TokenResponseDto =
        authService
            .getToken(
                grantType = "password",
                clientId = OAUTH_CLIENT_ID,
                clientSecret = OAUTH_CLIENT_SECRET,
                username = OAUTH_USERNAME,
                password = OAUTH_PASSWORD,
            )

    private suspend fun refreshTokenFromRemote(refreshToken: String): TokenResponseDto =
        authService
            .refreshToken(
                grantType = "refresh_token",
                clientId = OAUTH_CLIENT_ID,
                clientSecret = OAUTH_CLIENT_SECRET,
                refreshToken = refreshToken,
            )

    private companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"

        private const val OAUTH_CLIENT_ID = "APPTUSSAM"
        private const val OAUTH_CLIENT_SECRET = "8472a9bc-6114-4975-bc35-7267977c8850"
        private const val OAUTH_USERNAME = "infotus-usermobile@tussam.es"
        private const val OAUTH_PASSWORD = "Tu\$\$4m.-1nf0tu\$_U\$3rmob1l3"
    }

}