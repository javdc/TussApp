package com.javdc.tussapp.data.remote.api.interceptor

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import java.util.UUID

/**
 * The TUSSAM API rejects requests without a "deviceid" header with
 * 400 "Falta encabezado", even when the OAuth token is valid.
 *
 * The official AppTUSSAM app sends "A<versionName> - <random UUID>", where the
 * UUID is generated once per installation and persisted.
 */
class DeviceIdInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {

    private val deviceId: String by lazy { "A$OFFICIAL_APP_VERSION - ${getOrCreateUuid()}" }

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain.request().newBuilder()
                .header(HEADER_DEVICE_ID, deviceId)
                .build()
        )

    private fun getOrCreateUuid(): String =
        sharedPreferences.getString(KEY_DEVICE_UUID, null)
            ?: UUID.randomUUID().toString().also {
                sharedPreferences.edit().putString(KEY_DEVICE_UUID, it).apply()
            }

    private companion object {
        private const val HEADER_DEVICE_ID = "deviceid"
        private const val KEY_DEVICE_UUID = "device_uuid"

        // Version of the official AppTUSSAM app this header format was taken from
        private const val OFFICIAL_APP_VERSION = "5.0.6"
    }

}
