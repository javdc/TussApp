package com.javdc.tussapp.presentation.ui.stops

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.javdc.tussapp.common.util.serverZoneId
import com.javdc.tussapp.common.util.tryOrNull
import com.javdc.tussapp.presentation.R
import com.javdc.tussapp.presentation.databinding.FragmentLineDirectionsBinding
import com.javdc.tussapp.presentation.ui.common.BaseFragment
import com.javdc.tussapp.presentation.util.safeNavigate
import com.javdc.tussapp.presentation.util.setUpExitAndReenterTransitionsForContainerTransform
import com.javdc.tussapp.presentation.util.setUpPostponedEnterTransition
import com.javdc.tussapp.presentation.util.setUpSharedElementEnterTransitionForContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class LineDirectionsFragment : BaseFragment<FragmentLineDirectionsBinding>(FragmentLineDirectionsBinding::inflate) {

    private val args: LineDirectionsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpSharedElementEnterTransitionForContainerTransform()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPostponedEnterTransition(view)
        setUpToolbar()
        setUpDestinationsViewPager()
    }

    private fun setUpToolbar() {
        binding?.apply {
            lineDirectionsToolbar.title = getString(R.string.line_format, args.availableLine.label).ifBlank { getString(R.string.stops) }
            lineDirectionsToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setUpDestinationsViewPager() {
        binding?.apply {
            lineDirectionsDestinationsViewPager2.adapter =
                DirectionStopsPagerAdapter(
                    lineId = args.availableLine.id,
                    directions = args.availableLine.destinations.map { it.direction },
                    date = args.selectedDateTime?.let { tryOrNull { LocalDateTime.parse(it) } }
                        ?: LocalDateTime.now(serverZoneId)
                )

            TabLayoutMediator(lineDirectionsDestinationsTabLayout, lineDirectionsDestinationsViewPager2) { tab, position ->
                lineDirectionsDestinationsViewPager2.setCurrentItem(tab.position, true)
                tab.text = args.availableLine.destinations.getOrNull(position)?.let {
                    it.destinationName ?: getString(R.string.direction_format, it.direction)
                }
            }.attach()
        }
    }

    fun navigateToTimeEstimates(stopCode: Int, view: View) {
        setUpExitAndReenterTransitionsForContainerTransform()
        findNavController().safeNavigate(
            LineDirectionsFragmentDirections.actionLinesFragmentToTimeEstimatesFragment(stopCode),
            FragmentNavigatorExtras(view to getString(R.string.stop_estimates_transition_name))
        )
    }

    private inner class DirectionStopsPagerAdapter(
        val lineId: Int,
        val directions: List<Int>,
        val date: LocalDateTime
    ) : FragmentStateAdapter(this) {

        override fun getItemCount(): Int = directions.size

        override fun createFragment(position: Int): Fragment =
            DirectionStopsFragment.newInstance(lineId, directions[position], date)

    }

}