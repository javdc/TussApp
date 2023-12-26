package com.javdc.tussapp.presentation.ui.lines

import android.os.Bundle
import android.os.Parcel
import android.text.format.DateFormat.is24HourFormat
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.javdc.tussapp.common.util.toEpochMilliseconds
import com.javdc.tussapp.common.util.toLocalDateTime
import com.javdc.tussapp.domain.util.AsyncResult
import com.javdc.tussapp.presentation.R
import com.javdc.tussapp.presentation.databinding.FragmentLinesBinding
import com.javdc.tussapp.presentation.ui.common.BaseFragment
import com.javdc.tussapp.presentation.util.safeNavigate
import com.javdc.tussapp.presentation.util.setUpExitAndReenterTransitionsForContainerTransform
import com.javdc.tussapp.presentation.util.setUpPostponedEnterTransition
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class LinesFragment : BaseFragment<FragmentLinesBinding>(FragmentLinesBinding::inflate) {

    private val viewModel by viewModels<LinesViewModel>()

    private var adapter: LineAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPostponedEnterTransition(view)
        setUpLinesRecyclerView()
        setUpDatePicker()
        setUpTimePicker()
        observeLines()
    }

    private fun setUpDatePicker() {
        val dateValidator = object : DateValidator {
            override fun isValid(date: Long) =
                viewModel.isUtcTimestampSelectable(date)

            override fun describeContents() = 0

            override fun writeToParcel(p0: Parcel, p1: Int) {
                // no-op
            }
        }

        val calendarConstraints =
            CalendarConstraints.Builder()
                .setValidator(dateValidator)
                .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(viewModel.selectedDateTime.toLocalDate().toEpochMilliseconds(ZoneOffset.UTC))
            .setCalendarConstraints(calendarConstraints)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            val selectedDate = it.toLocalDateTime(ZoneOffset.UTC).toLocalDate()
            updateDateText(selectedDate)
            viewModel.fetchLines(LocalDateTime.of(selectedDate, viewModel.selectedDateTime.toLocalTime()))
        }

        binding?.linesDateChip?.setOnClickListener {
            if (!datePicker.isAdded) datePicker.show(childFragmentManager, null)
        }
    }

    private fun setUpTimePicker() {
        val clockFormat = if (is24HourFormat(context)) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setHour(viewModel.selectedDateTime.hour)
                .setMinute(viewModel.selectedDateTime.minute)
                .build()

        timePicker.addOnPositiveButtonClickListener {
            val selectedTime = LocalTime.of(timePicker.hour, timePicker.minute)
            updateTimeText(selectedTime)
            viewModel.fetchLines(LocalDateTime.of(viewModel.selectedDateTime.toLocalDate(), selectedTime))
        }

        binding?.linesTimeChip?.setOnClickListener {
            if (!timePicker.isAdded) timePicker.show(childFragmentManager, null)
        }
    }

    private fun setUpLinesRecyclerView() {
        adapter = LineAdapter { view, line ->
            binding?.linesRecyclerView?.animation = null
            viewModel.showRecyclerAnimation = false
            setUpExitAndReenterTransitionsForContainerTransform()
            findNavController().safeNavigate(
                LinesFragmentDirections.actionLinesFragmentToStopsFragment(line, viewModel.selectedDateTime.toString()),
                FragmentNavigatorExtras(view to getString(R.string.line_directions_transition_name))
            )
        }
        binding?.linesRecyclerView?.adapter = adapter
        if (viewModel.showRecyclerAnimation) {
            binding?.linesRecyclerView?.layoutAnimation =
                AnimationUtils.loadLayoutAnimation(context, R.anim.list_layout_animation)
        } else {
            binding?.linesRecyclerView?.layoutAnimation = null
            viewModel.showRecyclerAnimation = true
        }
    }

    private fun observeLines() {
        viewModel.linesLiveData.observe(viewLifecycleOwner) { result ->
            binding?.apply {
                when (result) {
                    is AsyncResult.Loading -> {
                        linesCircularProgressIndicator.show()
                        linesErrorView.hide()
                    }

                    is AsyncResult.Error -> {
                        linesCircularProgressIndicator.hide()
                        linesErrorView.show(result.error) { viewModel.retry() }
                        updateDateText(viewModel.lastTriedDateTime.toLocalDate())
                        updateTimeText(viewModel.lastTriedDateTime.toLocalTime())
                        adapter?.submitList(emptyList())
                    }

                    is AsyncResult.Success -> {
                        linesCircularProgressIndicator.hide()
                        linesErrorView.hide()
                        result.data.date?.let {
                            updateDateText(it.toLocalDate())
                            updateTimeText(it.toLocalTime())
                        }
                        val listWasEmpty = adapter?.itemCount == 0
                        adapter?.submitList(result.data.availableLines) {
                            if (listWasEmpty) {
                                linesRecyclerView.scheduleLayoutAnimation()
                            } else {
                                linesRecyclerView.smoothScrollToPosition(0)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateDateText(date: LocalDate) {
        binding?.linesDateChip?.apply {
            isVisible = true
            text = date.format(DateTimeFormatter.ofPattern(getString(R.string.date_format_day_month_year)))
        }
    }

    private fun updateTimeText(time: LocalTime) {
        binding?.linesTimeChip?.apply {
            isVisible = true
            text = time.format(DateTimeFormatter.ofPattern(getString(R.string.date_format_hours_minutes)))
        }
    }

}