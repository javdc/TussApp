package com.javdc.tussapp.presentation.util

import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFade
import com.javdc.tussapp.presentation.R

fun Fragment.setUpSharedElementEnterTransitionForContainerTransform() {
    sharedElementEnterTransition = MaterialContainerTransform().apply {
        drawingViewId = R.id.mainNavHostFragment
    }
}

fun Fragment.setUpExitAndReenterTransitionsForContainerTransform() {
    exitTransition = MaterialElevationScale(false)
    reenterTransition = MaterialFade()
}

fun Fragment.clearExitAndReenterTransitions() {
    exitTransition = null
    reenterTransition = null
}

fun Fragment.setUpPostponedEnterTransition(view: View) {
    postponeEnterTransition()
    view.doOnPreDraw { startPostponedEnterTransition() }
    view.postDelayed({ clearExitAndReenterTransitions() }, 50)
}