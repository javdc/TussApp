package com.javdc.tussapp.presentation.ui.common

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.javdc.tussapp.common.util.takeIfNotBlank
import com.javdc.tussapp.domain.util.AsyncError
import com.javdc.tussapp.presentation.R
import com.javdc.tussapp.presentation.databinding.ViewErrorBinding

class ErrorView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    var binding = ViewErrorBinding.inflate(LayoutInflater.from(context), this, true)

    var finalVisibilityAfterAnimation: Int? = null

    fun show(error: AsyncError? = null, listener: (() -> Unit)? = null) {
        binding.apply {
            when (error) {
                is AsyncError.ServerError -> {
                    errorViewTitleTextView.text = context.getString(R.string.error_generic_with_http_code, error.code)
                    errorViewMessageTextView.text = error.errorMessage?.takeIfNotBlank() ?: context.getString(
                        R.string.error_generic_description)
                }
                is AsyncError.ConnectionError -> {
                    errorViewTitleTextView.text = context.getString(R.string.error_connection_error)
                    errorViewMessageTextView.text = context.getString(R.string.error_connection_error_description)
                }
                is AsyncError.TimeoutError -> {
                    errorViewTitleTextView.text = context.getString(R.string.error_timeout_error)
                    errorViewMessageTextView.text = context.getString(R.string.error_timeout_error_description)
                }
                is AsyncError.EmptyResponseError -> {
                    errorViewTitleTextView.text = context.getString(R.string.error_empty_response_error)
                    errorViewMessageTextView.text = context.getString(R.string.error_empty_response_error_description)
                }
                else -> {
                    errorViewTitleTextView.text = context.getString(R.string.error_generic)
                    errorViewMessageTextView.text = context.getString(R.string.error_generic_description)
                }
            }
            listener?.let {
                errorViewRetryButton.setOnClickListener { listener() }
                errorViewRetryButton.isVisible = true
            } ?: run {
                errorViewRetryButton.setOnClickListener(null)
                errorViewRetryButton.isVisible = false
            }
        }
        finalVisibilityAfterAnimation = View.VISIBLE
        alpha = 0f
        visibility = View.VISIBLE
        animate()
            .alpha(1f)
            .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    finalVisibilityAfterAnimation?.let { visibility = it }
                }
            })
    }

    fun hide() {
        finalVisibilityAfterAnimation = View.GONE
        animate()
            .alpha(0f)
            .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    finalVisibilityAfterAnimation?.let { visibility = it }
                }
            })
    }

}