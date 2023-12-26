package com.javdc.tussapp.presentation.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import co.touchlab.kermit.Logger
import java.net.URLEncoder

fun openPositionInMap(context: Context?, latLng: Pair<Double, Double>, pointName: String?): Boolean {
    return try {
        val locationUriString = buildString {
            append("geo:${latLng.first},${latLng.second}")
            pointName?.let { append("?q=${URLEncoder.encode(pointName, "utf-8")}") }
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(locationUriString))
        context!!.startActivity(intent)
        true

    } catch (e: Exception) {
        Logger.e("Couldn't open geo uri", e, "openPositionInMap")
        false
    }
}

fun openInCustomTab(context: Context?, url: String?): Boolean {
    return try {
        CustomTabsIntent.Builder()
            .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
            .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
            .build()
            .launchUrl(context!!, Uri.parse(url))
        true

    } catch (e: Exception) {
        Logger.e("Couldn't open url in custom tab", e, "openInCustomTab")
        false
    }
}