package com.javdc.tussapp.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.search.SearchView
import com.javdc.tussapp.domain.util.AsyncResult
import com.javdc.tussapp.presentation.R
import com.javdc.tussapp.presentation.databinding.FragmentHomeBinding
import com.javdc.tussapp.presentation.ui.common.BaseFragment
import com.javdc.tussapp.presentation.util.safeNavigate
import com.javdc.tussapp.presentation.util.setUpExitAndReenterTransitionsForContainerTransform
import com.javdc.tussapp.presentation.util.setUpPostponedEnterTransition
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()

    private var noticeAdapter: NoticeAdapter? = null
    private var favoriteStopAdapter: FavoriteStopAdapter? = null
    private var stopSuggestionAdapter: StopSuggestionAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPostponedEnterTransition(view)
        setUpToolbar()
        setUpSearch()
        setUpNoticesRecyclerView()
        setUpFavoriteStopsRecyclerView()
        setUpStopSuggestionsRecyclerView()
        observeFavoriteStops()
        observeNotices()
        observeStopSuggestions()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshNoticesIfNeeded()
    }

    private fun setUpToolbar() {
        binding?.apply {
            homeStopsSearchBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_about -> {
                        findNavController().safeNavigate(HomeFragmentDirections.actionHomeFragmentToAboutFragment())
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    private fun setUpNoticesRecyclerView() {
        noticeAdapter = NoticeAdapter()
        binding?.homeNoticesRecyclerView?.adapter = noticeAdapter
    }

    private fun setUpFavoriteStopsRecyclerView() {
        favoriteStopAdapter = FavoriteStopAdapter { stopCode, view ->
            navigateToTimeEstimates(stopCode, view)
        }
        binding?.homeFavoriteStopsRecyclerView?.adapter = favoriteStopAdapter
    }

    private fun setUpStopSuggestionsRecyclerView() {
        stopSuggestionAdapter = StopSuggestionAdapter { stopCode, view ->
            navigateToTimeEstimates(stopCode, view)
        }
        binding?.homeStopSuggestionsRecyclerView?.adapter = stopSuggestionAdapter
    }

    private fun setUpSearch() {
        binding?.apply {
            val onBackPressedCallback = object : OnBackPressedCallback(false) {
                override fun handleOnBackPressed() {
                    homeSearchView.hide()
                }
            }
            activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
            homeSearchView.addTransitionListener { _, _, newState ->
                onBackPressedCallback.isEnabled = newState == SearchView.TransitionState.SHOWN
                if (newState == SearchView.TransitionState.HIDDEN) homeStopSuggestionsRecyclerView.scrollToPosition(0)
            }

            homeSearchView.editText.addTextChangedListener { text ->
                viewModel.filterStopSuggestions(text.toString())
            }

            homeSearchView.editText.setOnEditorActionListener { textView, _, _ ->
                val searchInput = textView.text.toString()
                searchInput.toIntOrNull()?.let { code ->
                    navigateToTimeEstimates(code, homeSearchView)
                    return@setOnEditorActionListener true
                } ?: run {
                    if (stopSuggestionAdapter?.itemCount == 1) {
                        stopSuggestionAdapter?.getItem(0)?.let { stop ->
                            navigateToTimeEstimates(stop.code, homeSearchView)
                            return@setOnEditorActionListener true
                        }
                    }
                }
                false
            }
        }
    }

    private fun observeFavoriteStops() {
        viewModel.favoriteStopsLiveData.observe(viewLifecycleOwner) { favoriteStops ->
            binding?.apply {
                favoriteStopAdapter?.submitList(favoriteStops)
                homeFavoriteStopsNoFavoritesTextView.isVisible = favoriteStops.isEmpty()
                homeFavoriteStopsRecyclerView.isVisible = favoriteStops.isNotEmpty()
            }
        }
    }

    private fun observeStopSuggestions() {
        viewModel.stopSuggestionsLiveData.observe(viewLifecycleOwner) { suggestions ->
            stopSuggestionAdapter?.submitList(suggestions)
        }
    }

    private fun observeNotices() {
        viewModel.noticesLiveData.observe(viewLifecycleOwner) { result ->
            binding?.apply {
                when (result) {
                    is AsyncResult.Loading -> {
                        // no-op
                    }
                    is AsyncResult.Error -> {
                        homeNoticesInfoTextView.isVisible = true
                        homeNoticesInfoTextView.setText(R.string.home_notices_error)
                    }
                    is AsyncResult.Success -> {
                        if (result.data.isEmpty()) {
                            homeNoticesInfoTextView.isVisible = true
                            homeNoticesInfoTextView.setText(R.string.home_notices_empty)
                        } else {
                            homeNoticesInfoTextView.isVisible = false
                            homeNoticesInfoTextView.text = null
                        }
                        noticeAdapter?.submitList(result.data)
                    }
                }
            }
        }
    }

    private fun navigateToTimeEstimates(stopCode: Int, viewForAnimation: View) {
        setUpExitAndReenterTransitionsForContainerTransform()
        findNavController().safeNavigate(
            HomeFragmentDirections.actionHomeFragmentToTimeEstimatesFragment(stopCode),
            FragmentNavigatorExtras(viewForAnimation to getString(R.string.stop_estimates_transition_name))
        )
    }

}