package com.javdc.tussapp.presentation.ui.cards

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.javdc.tussapp.common.util.takeIfNotBlank
import com.javdc.tussapp.domain.util.AsyncError
import com.javdc.tussapp.domain.util.AsyncResult
import com.javdc.tussapp.presentation.R
import com.javdc.tussapp.presentation.databinding.DialogAddCardBinding
import com.javdc.tussapp.presentation.ui.common.BaseDialogFragment
import com.javdc.tussapp.presentation.util.requestFocusWithKeyboard
import com.javdc.tussapp.presentation.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditCardDialogFragment : BaseDialogFragment<DialogAddCardBinding>(DialogAddCardBinding::inflate) {

    private val viewModel by hiltNavGraphViewModels<CardsViewModel>(R.id.cards_navigation)
    private val args: AddEditCardDialogFragmentArgs by navArgs()

    private val positiveButton: Button?
        get() = (dialog as? AlertDialog)?.getButton(Dialog.BUTTON_POSITIVE)

    override fun createDialog(): Dialog {
        return MaterialAlertDialogBuilder(requireActivity())
            .setView(binding?.root)
            .setTitle(if (args.isEdition) R.string.edit_card_dialog_title else R.string.add_card_dialog_title)
            .setPositiveButton(if (args.isEdition) R.string.action_save else R.string.action_add, null) // We set listener later to prevent undesired dismiss
            .create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            args.cardNumber?.let { addCardCardNumberTextInputEditText.setText(it) }
            args.customName?.let { addCardCustomNameTextInputEditText.setText(it) }
            args.errorMessage?.let { addCardCardNumberTextInputLayout.error = it }

            addCardCardNumberTextInputLayout.isEnabled = !args.isEdition

            addCardCardNumberTextInputEditText.addTextChangedListener {
                addCardCardNumberTextInputLayout.isErrorEnabled = false
            }
        }
        observeAddCard()
    }

    override fun onStart() {
        super.onStart()
        binding?.apply {
            (if (args.isEdition) addCardCustomNameTextInputEditText else addCardCardNumberTextInputEditText).requestFocusWithKeyboard()

            positiveButton?.setOnClickListener {
                addCardCardNumberTextInputEditText.text?.toString()?.toLongOrNull()?.let { cardNumber ->
                    viewModel.addCardToLocal(
                        cardNumber = cardNumber,
                        customName = addCardCustomNameTextInputEditText.text?.toString()
                    )
                } ?: run {
                    addCardCardNumberTextInputLayout.error = getString(R.string.error_add_card_empty)
                }
            }
        }
    }

    private fun observeAddCard() {
        viewModel.addCardLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is AsyncResult.Loading -> {
                        binding?.addCardLinearProgressIndicator?.show()
                        positiveButton?.isEnabled = false
                    }
                    is AsyncResult.Error -> {
                        positiveButton?.isEnabled = true
                        binding?.apply {
                            addCardLinearProgressIndicator.hide()
                            addCardCardNumberTextInputLayout.error = (result.error as? AsyncError.ServerError)?.errorMessage?.takeIfNotBlank() ?: getString(R.string.error_add_card)
                        }
                    }
                    is AsyncResult.Success -> {
                        positiveButton?.isEnabled = true
                        binding?.addCardLinearProgressIndicator?.hide()
                        showSnackbar(R.string.success_add_card)
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

}