package com.javdc.tussapp.presentation.ui.stops

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javdc.tussapp.domain.model.LineStopBo
import com.javdc.tussapp.domain.usecase.GetLineDirectionStopsUseCase
import com.javdc.tussapp.domain.util.AsyncResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DirectionStopsViewModel @Inject constructor(
    private val getLineDirectionStopsUseCase: GetLineDirectionStopsUseCase,
) : ViewModel() {

    private var stopsAlreadyLoaded = false

    private val _stopsLiveData = MutableLiveData<AsyncResult<List<LineStopBo>>>()
    val stopsLiveData: LiveData<AsyncResult<List<LineStopBo>>>
        get() = _stopsLiveData

    fun fetchStopsIfNeeded(lineId: Int, direction: Int, date: LocalDateTime) {
        if (!stopsAlreadyLoaded) {
            fetchStops(lineId, direction, date)
            stopsAlreadyLoaded = true
        }
    }

    private fun fetchStops(lineId: Int, direction: Int, date: LocalDateTime) {
        viewModelScope.launch(Dispatchers.IO) {
            getLineDirectionStopsUseCase(lineId, direction, date).collect {
                _stopsLiveData.postValue(it)
            }
        }
    }

}