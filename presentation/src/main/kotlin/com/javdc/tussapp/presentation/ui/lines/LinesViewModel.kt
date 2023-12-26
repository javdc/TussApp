package com.javdc.tussapp.presentation.ui.lines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javdc.tussapp.common.util.serverZoneId
import com.javdc.tussapp.common.util.toEpochMilliseconds
import com.javdc.tussapp.domain.usecase.GetAvailableDatesUseCase
import com.javdc.tussapp.domain.usecase.GetLinesUseCase
import com.javdc.tussapp.domain.util.AsyncResult
import com.javdc.tussapp.domain.util.map
import com.javdc.tussapp.presentation.mapper.toVo
import com.javdc.tussapp.presentation.model.LinesInfoVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class LinesViewModel @Inject constructor(
    private val getAvailableDatesUseCase: GetAvailableDatesUseCase,
    private val getLinesUseCase: GetLinesUseCase,
) : ViewModel() {

    private val _linesLiveData = MutableLiveData<AsyncResult<LinesInfoVo>>()
    val linesLiveData: LiveData<AsyncResult<LinesInfoVo>>
        get() = _linesLiveData

    private var availableDatesUtcTimestamps = emptyList<Long>()

    var selectedDateTime: LocalDateTime = LocalDateTime.now(serverZoneId)
        private set

    var lastTriedDateTime: LocalDateTime = selectedDateTime
        private set

    var showRecyclerAnimation = true

    init {
        observeAvailableDatesFromLocal()
        fetchLines(selectedDateTime)
    }

    fun fetchLines(date: LocalDateTime) {
        viewModelScope.launch(Dispatchers.IO) {
            lastTriedDateTime = date
            getLinesUseCase(date).collect { result ->
                if (result is AsyncResult.Success) {
                    selectedDateTime = result.data.date ?: lastTriedDateTime
                    lastTriedDateTime = selectedDateTime
                }
                _linesLiveData.postValue(result.map { it.toVo() })
            }
        }
    }

    fun retry() {
        fetchLines(lastTriedDateTime)
    }

    fun isUtcTimestampSelectable(utcTimestamp: Long) = availableDatesUtcTimestamps.contains(utcTimestamp)

    private fun observeAvailableDatesFromLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            getAvailableDatesUseCase().collect { dates ->
                availableDatesUtcTimestamps = dates.map { it.toEpochMilliseconds(ZoneOffset.UTC) }
            }
        }
    }
}