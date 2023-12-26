package com.javdc.tussapp.presentation.ui.common

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

abstract class BaseDialogFragment<VB: ViewBinding>(private val inflate: (LayoutInflater) -> VB) : DialogFragment() {

    protected var binding: VB? = null

    /**
     * Dialog that will be returned in onCreateDialog
     */
    abstract fun createDialog(): Dialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = inflate(layoutInflater)
        return createDialog()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
