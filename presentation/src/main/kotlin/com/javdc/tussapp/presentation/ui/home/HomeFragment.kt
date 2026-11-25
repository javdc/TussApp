package com.javdc.tussapp.presentation.ui.home

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.search.SearchView
import com.javdc.tussapp.domain.util.AsyncResult
import com.javdc.tussapp.presentation.R
import com.javdc.tussapp.presentation.databinding.FragmentHomeBinding
import com.javdc.tussapp.presentation.ui.common.BaseFragment
import com.javdc.tussapp.presentation.util.safeNavigate
import com.javdc.tussapp.presentation.util.showSnackbar
import com.javdc.tussapp.presentation.util.setUpExitAndReenterTransitionsForContainerTransform
import com.javdc.tussapp.presentation.util.setUpPostponedEnterTransition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()

    private var noticeAdapter: NoticeAdapter? = null
    private var favoriteStopAdapter: FavoriteStopAdapter? = null
    private var stopSuggestionAdapter: StopSuggestionAdapter? = null

    // Lets the user pick where to save the file (Downloads, Drive, etc.)
    private val exportFavoritesLauncher =
        registerForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
            uri?.let { exportFavoriteStops(it) }
        }

    private val importFavoritesLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let { importFavoriteStops(it) }
        }

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
                    R.id.home_export_favorites -> {
                        if (viewModel.favoriteStopsLiveData.value.isNullOrEmpty()) {
                            showSnackbar(R.string.export_favorites_empty)
                        } else {
                            exportFavoritesLauncher.launch("tussapp_favoritos_${LocalDate.now()}.json")
                        }
                        true
                    }
                    R.id.home_import_favorites -> {
                        // Some file managers don't tag .json files as application/json
                        importFavoritesLauncher.launch(arrayOf("application/json", "text/plain", "application/octet-stream"))
                        true
                    }
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

    private fun exportFavoriteStops(uri: Uri) {
        val stopCodes = viewModel.favoriteStopsLiveData.value.orEmpty().map { it.code }.sorted()
        val json = JSONObject()
            .put("favorite_stops", JSONArray(stopCodes))
            .toString(2)
        val contentResolver = requireContext().contentResolver

        viewLifecycleOwner.lifecycleScope.launch {
            val success = withContext(Dispatchers.IO) {
                runCatching {
                    contentResolver.openOutputStream(uri)?.use { it.write(json.toByteArray()) }
                        ?: error("Could not open output stream")
                }.isSuccess
            }
            showSnackbar(if (success) R.string.export_favorites_success else R.string.export_favorites_error)
        }
    }

    private fun importFavoriteStops(uri: Uri) {
        val contentResolver = requireContext().contentResolver

        viewLifecycleOwner.lifecycleScope.launch {
            val stopCodes = withContext(Dispatchers.IO) {
                runCatching {
                    val text = contentResolver.openInputStream(uri)?.use { it.bufferedReader().readText() }
                        ?: error("Could not open input stream")
                    parseFavoriteStops(text)
                }.getOrNull()
            }

            if (stopCodes.isNullOrEmpty()) {
                showSnackbar(R.string.import_favorites_error)
            } else {
                viewModel.importFavoriteStops(stopCodes)
                showSnackbar(getString(R.string.import_favorites_success, stopCodes.size))
            }
        }
    }

    /**
     * Accepts the exported format `{"favorite_stops": [123, 456]}`
     * and also a plain array `[123, 456]`.
     */
    private fun parseFavoriteStops(text: String): Set<Int> {
        val trimmed = text.trim()
        val array = if (trimmed.startsWith("[")) {
            JSONArray(trimmed)
        } else {
            JSONObject(trimmed).getJSONArray("favorite_stops")
        }
        return (0 until array.length())
            .mapNotNull { array.optString(it).trim().toIntOrNull() }
            .filter { it > 0 }
            .toSet()
    }

    private fun navigateToTimeEstimates(stopCode: Int, viewForAnimation: View) {
        setUpExitAndReenterTransitionsForContainerTransform()
        findNavController().safeNavigate(
            HomeFragmentDirections.actionHomeFragmentToTimeEstimatesFragment(stopCode),
            FragmentNavigatorExtras(viewForAnimation to getString(R.string.stop_estimates_transition_name))
        )
    }

}