package com.javdc.tussapp.presentation.ui.stops

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.javdc.tussapp.common.util.safeLet
import com.javdc.tussapp.domain.util.AsyncResult
import com.javdc.tussapp.presentation.databinding.FragmentDirectionStopsBinding
import com.javdc.tussapp.presentation.ui.common.BaseFragment
import com.javdc.tussapp.presentation.util.getSerializableCompat
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class DirectionStopsFragment : BaseFragment<FragmentDirectionStopsBinding>(FragmentDirectionStopsBinding::inflate) {

    private val viewModel by viewModels<DirectionStopsViewModel>()

    private var adapter: LineStopAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpDestinationsRecycler()
        fetchStopsIfNeeded()
        observeStops()
    }

    private fun setUpDestinationsRecycler() {
        adapter = LineStopAdapter { stopCode, view ->
            (parentFragment as? LineDirectionsFragment)?.navigateToTimeEstimates(stopCode, view)
        }
        binding?.directionStopsRecyclerView?.adapter = adapter
    }

    private fun observeStops() {
        viewModel.stopsLiveData.observe(viewLifecycleOwner) { result ->
            binding?.apply {
                when (result) {
                    is AsyncResult.Loading -> {
                        directionStopsCircularProgressIndicator.show()
                        directionStopsErrorView.hide()
                    }
                    is AsyncResult.Error -> {
                        directionStopsCircularProgressIndicator.hide()
                        adapter?.submitList(emptyList())
                        directionStopsErrorView.show(result.error) { fetchStopsIfNeeded() }
                    }
                    is AsyncResult.Success -> {
                        directionStopsCircularProgressIndicator.hide()
                        adapter?.submitList(result.data)
                        directionStopsErrorView.hide()
                    }
                }
            }
        }
    }

    private fun fetchStopsIfNeeded() {
        safeLet(
            arguments?.getInt(LINE_ID_ARG),
            arguments?.getInt(DIRECTION_ARG),
            arguments?.getSerializableCompat(DATE_ARG, LocalDateTime::class.java)
        ) { lineId, direction, date ->
            viewModel.fetchStopsIfNeeded(lineId, direction, date)
        }
    }

    companion object {
        private const val LINE_ID_ARG = "lineId"
        private const val DIRECTION_ARG = "direction"
        private const val DATE_ARG = "date"

        fun newInstance(lineId: Int, direction: Int, date: LocalDateTime) =
            DirectionStopsFragment().apply {
                arguments = Bundle().apply {
                    putInt(LINE_ID_ARG, lineId)
                    putInt(DIRECTION_ARG, direction)
                    putSerializable(DATE_ARG, date)
                }
            }
    }

}