package com.javdc.tussapp.presentation.ui.cards

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.javdc.tussapp.domain.util.AsyncResult
import com.javdc.tussapp.presentation.R
import com.javdc.tussapp.presentation.databinding.FragmentCardsBinding
import com.javdc.tussapp.presentation.ui.common.BaseFragment
import com.javdc.tussapp.presentation.util.openInCustomTab
import com.javdc.tussapp.presentation.util.safeNavigate
import com.javdc.tussapp.presentation.util.showSimpleDialog
import com.javdc.tussapp.presentation.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardsFragment : BaseFragment<FragmentCardsBinding>(FragmentCardsBinding::inflate) {

    private val viewModel by hiltNavGraphViewModels<CardsViewModel>(R.id.cards_navigation)

    private var adapter: CardAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpCardsRecyclerView()
        setUpAddCardFloatingActionButton()
        observeCards()
        observeCardsSyncStatus()
        observeSuccessfullyAddedCardNumber()
    }

    private fun setUpToolbar() {
        binding?.apply {
            cardsToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.cards_recharge -> {
                        openInCustomTab(context, "https://recargas.tussam.es/").also { success ->
                            if (!success) showSnackbar(R.string.error_no_browser_installed)
                        }
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    private fun setUpCardsRecyclerView() {
        binding?.apply {
            adapter = CardAdapter(
                onClickEdit = {
                    findNavController().safeNavigate(
                        CardsFragmentDirections.actionCardsFragmentToAddEditCardDialog(true, it.cardNumber.toString(), it.customName)
                    )
                },
                onClickDelete = {
                    showSimpleDialog(
                        title = R.string.confirm_delete_card_title,
                        message = R.string.confirm_delete_card_message,
                        positiveText = R.string.action_delete,
                        negativeText = R.string.action_cancel,
                    ) { _, _ ->
                        viewModel.removeCardStatusFromLocal(it.cardNumber)
                    }
                }
            )
            cardsRecyclerView.adapter = adapter
            cardsSwipeRefreshLayout.setOnRefreshListener {
                viewModel.syncCards()
            }
        }
    }

    private fun setUpAddCardFloatingActionButton() {
        binding?.apply {
            cardsAddFloatingActionButton.setOnClickListener {
                findNavController().safeNavigate(
                    CardsFragmentDirections.actionCardsFragmentToAddEditCardDialog()
                )
            }
        }
    }

    private fun observeCards() {
        viewModel.cardsLiveData.observe(viewLifecycleOwner) { cards ->
            adapter?.submitList(cards)
            binding?.cardsEmptyTextView?.isVisible = cards.isEmpty()
        }
    }

    private fun observeCardsSyncStatus() {
        viewModel.cardsSyncStatusLiveData.observe(viewLifecycleOwner) { result ->
            binding?.apply {
                cardsSwipeRefreshLayout.isRefreshing = result is AsyncResult.Loading
                if (result is AsyncResult.Error) {
                    showSnackbar(R.string.error_sync_cards)
                }
            }
        }
    }

    private fun observeSuccessfullyAddedCardNumber() {
        viewModel.successfullyAddedCardNumberLiveData.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                showSnackbar(R.string.success_add_card)
            }
        }
    }

}