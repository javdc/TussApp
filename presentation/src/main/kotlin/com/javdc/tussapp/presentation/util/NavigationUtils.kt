package com.javdc.tussapp.presentation.util

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import co.touchlab.kermit.Logger

/**
 * Good old navigate, but wrapped in a try-catch block. This is used to avoid crashes when,
 * for example, double tapping a button that navigates to another screen.
 */
fun NavController.safeNavigate(directions: NavDirections, navOptions: NavOptions? = null) {
    try {
        navigate(directions, navOptions)

    } catch (e: Throwable) {
        Logger.e("Caught exception when trying to navigate to direction", e, "safeNavigate")
    }
}

/**
 * Good old navigate, but wrapped in a try-catch block. This is used to avoid crashes when,
 * for example, double tapping a button that navigates to another screen.
 */
fun NavController.safeNavigate(directions: NavDirections, navigatorExtras: Navigator.Extras) {
    try {
        navigate(directions, navigatorExtras)

    } catch (e: Throwable) {
        Logger.e("Caught exception when trying to navigate to direction", e, "safeNavigate")
    }
}