package com.javdc.tussapp.presentation.util

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable

fun Fragment.showSnackbar(@StringRes text: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    showSnackbar(getString(text), duration)
}

fun Fragment.showSnackbar(text: String?, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view ?: return, text ?: return, duration).show()
}

fun Fragment.showToast(@StringRes text: Int, duration: Int = Toast.LENGTH_SHORT) {
    showToast(getString(text), duration)
}

fun Fragment.showToast(text: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context ?: return, text ?: return, duration).show()
}

fun Fragment.showSimpleDialog(@StringRes title: Int?, @StringRes message: Int?, @StringRes positiveText: Int? = null, @StringRes negativeText: Int? = null, onClickListener: DialogInterface.OnClickListener? = null) {
    showSimpleDialog(
        title = title?.let { getString(it) },
        message = message?.let { getString(it) },
        positiveText = positiveText?.let { getString(it) },
        negativeText = negativeText?.let { getString(it) },
        onClickListener = onClickListener,
    )
}

fun Fragment.showSimpleDialog(title: String?, message: String?, positiveText: String? = null, negativeText: String? = null, onClickListener: DialogInterface.OnClickListener? = null) {
    MaterialAlertDialogBuilder(context ?: return).apply {
        title?.let { setTitle(it) }
        message?.let { setMessage(it) }
        positiveText?.let {
            setPositiveButton(it, onClickListener)
        } ?: run {
            setPositiveButton(android.R.string.ok, null)
        }
        negativeText?.let { setNegativeButton(negativeText, null) }
    }.show()
}

fun <T : Serializable> Bundle.getSerializableCompat(key: String, clazz: Class<T>): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, clazz)
    } else {
        @Suppress("UNCHECKED_CAST", "DEPRECATION")
        getSerializable(key) as? T
    }