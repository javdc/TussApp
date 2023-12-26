package com.javdc.tussapp.presentation.ui.estimates

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.javdc.tussapp.domain.util.AsyncResult
import com.javdc.tussapp.presentation.R
import com.javdc.tussapp.presentation.databinding.FragmentStopEstimatesBinding
import com.javdc.tussapp.presentation.ui.common.BaseFragment
import com.javdc.tussapp.presentation.util.openPositionInMap
import com.javdc.tussapp.presentation.util.setUpSharedElementEnterTransitionForContainerTransform
import com.javdc.tussapp.presentation.util.showSimpleDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import kotlin.concurrent.schedule

@AndroidEntryPoint
class StopEstimatesFragment : BaseFragment<FragmentStopEstimatesBinding>(FragmentStopEstimatesBinding::inflate) {

    private val viewModel by viewModels<StopEstimatesViewModel>()
    private val args: StopEstimatesFragmentArgs by navArgs()

    private var adapter: StopEstimateAdapter? = null

    private var updateStopEstimatesTimer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpSharedElementEnterTransitionForContainerTransform()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpTimeEstimatesRecyclerView()
        setUpSwipeToRefresh()
        observeStopTimes()
        observeFavoriteStatus()
        viewModel.observeFavoriteStatusFromLocal(args.stopCode)
    }

    override fun onResume() {
        super.onResume()
        updateStopEstimatesTimer = Timer().apply {
            schedule(0L, 30000L) {
                viewModel.fetchEstimates(args.stopCode)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        updateStopEstimatesTimer?.cancel()
        updateStopEstimatesTimer = null
    }

    private fun setUpToolbar() {
        binding?.apply {
            stopEstimatesToolbar.title = getString(R.string.stop_format, args.stopCode).ifBlank { getString(R.string.stop_estimates) }
            stopEstimatesToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            stopEstimatesToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.stop_estimates_favorite -> {
                        onClickFavoriteMenuItem(it)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    private fun setUpTimeEstimatesRecyclerView() {
        adapter = StopEstimateAdapter()
        binding?.stopEstimatesRecyclerView?.adapter = adapter
    }

    private fun setUpSwipeToRefresh() {
        binding?.apply {
            stopEstimatesSwipeRefreshLayout.setOnRefreshListener {
                viewModel.fetchEstimates(args.stopCode)
            }
        }
    }

    private fun onClickFavoriteMenuItem(menuItem: MenuItem) {
        if (menuItem.isChecked) {
            viewModel.removeStopFromFavorites(args.stopCode)
        } else {
            viewModel.addStopToFavorites(args.stopCode)
        }
    }

    private fun observeStopTimes() {
        viewModel.stopEstimatesLiveData.observe(viewLifecycleOwner) { result ->
            binding?.apply {
                when (result) {
                    is AsyncResult.Loading -> {
                        if (!stopEstimatesSwipeRefreshLayout.isRefreshing) {
                            stopEstimatesCircularProgressIndicator.show()
                        }
                        stopEstimatesErrorView.hide()
                    }
                    is AsyncResult.Error -> {
                        stopEstimatesSwipeRefreshLayout.isRefreshing = false
                        stopEstimatesCircularProgressIndicator.hide()
                        stopEstimatesStopNameTextView.text = null
                        setUpMapMenuIcon(null, null)
                        adapter?.submitList(emptyList())
                        stopEstimatesErrorView.show(result.error) {
                            viewModel.fetchEstimates(args.stopCode)
                        }
                    }
                    is AsyncResult.Success -> {
                        stopEstimatesSwipeRefreshLayout.isRefreshing = false
                        stopEstimatesCircularProgressIndicator.hide()
                        result.data.let { estimatesInfo ->
                            stopEstimatesStopNameTextView.text = estimatesInfo.description
                            setUpMapMenuIcon(estimatesInfo.latLng, estimatesInfo.description)
                            val listWasEmpty = adapter?.itemCount == 0
                            adapter?.submitList(estimatesInfo.estimates) {
                                if (listWasEmpty) {
                                    stopEstimatesRecyclerView.scheduleLayoutAnimation()
                                } else {
                                    stopEstimatesRecyclerView.smoothScrollToPosition(0)
                                }
                            }
                        }
                        stopEstimatesErrorView.hide()
                    }
                }
            }
        }
    }

    private fun observeFavoriteStatus() {
        viewModel.stopFavoriteStatusLiveData.observe(viewLifecycleOwner) { isFavorite ->
            binding?.stopEstimatesToolbar?.menu?.children
                ?.find { it.itemId == R.id.stop_estimates_favorite }
                ?.apply {
                    isVisible = true
                    isChecked = isFavorite
                    setIcon(if (isFavorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off)
                    setTitle(if (isFavorite) R.string.action_remove_from_favorites else R.string.action_add_to_favorites)
                }
        }
    }

    private fun setUpMapMenuIcon(latLng: Pair<Double, Double>?, pointName: String?) {
        binding?.stopEstimatesToolbar?.menu?.children
            ?.find { it.itemId == R.id.stop_estimates_open_in_map }
            ?.let { menuItem ->
                latLng?.let {
                    menuItem.isVisible = true
                    menuItem.setOnMenuItemClickListener {
                        openPositionInMap(context, latLng, pointName).also { success ->
                            if (!success) {
                                showSimpleDialog(
                                    title = R.string.error_opening_map_dialog_title,
                                    message = R.string.error_opening_map_dialog_body,
                                )
                            }
                        }
                    }

                } ?: run {
                    menuItem.isVisible = false
                    menuItem.setOnMenuItemClickListener(null)
                }
            }
    }

}