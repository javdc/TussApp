package com.javdc.tussapp.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javdc.tussapp.domain.model.FavoriteStopBo
import com.javdc.tussapp.domain.model.NoticeBo
import com.javdc.tussapp.domain.model.StopBo
import com.javdc.tussapp.domain.usecase.GetFavoriteStopsUseCase
import com.javdc.tussapp.domain.usecase.GetNoticesUseCase
import com.javdc.tussapp.domain.usecase.GetStopsWithLinesFromLocalUseCase
import com.javdc.tussapp.domain.usecase.SyncAllUseCase
import com.javdc.tussapp.domain.util.AsyncResult
import com.javdc.tussapp.presentation.util.normalize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNoticesUseCase: GetNoticesUseCase,
    private val getFavoriteStopsUseCase: GetFavoriteStopsUseCase,
    private val syncAllUseCase: SyncAllUseCase,
    private val getStopsWithLinesFromLocalUseCase: GetStopsWithLinesFromLocalUseCase,
) : ViewModel() {

    private val _noticesLiveData = MutableLiveData<AsyncResult<List<NoticeBo>>>()
    val noticesLiveData: LiveData<AsyncResult<List<NoticeBo>>>
        get() = _noticesLiveData

    private val _favoriteStopsLiveData = MutableLiveData<List<FavoriteStopBo>>()
    val favoriteStopsLiveData: LiveData<List<FavoriteStopBo>>
        get() = _favoriteStopsLiveData

    private val _stopSuggestionsLiveData = MutableLiveData<List<StopBo>>()
    val stopSuggestionsLiveData: LiveData<List<StopBo>>
        get() = _stopSuggestionsLiveData

    private var lastNoticesUpdateMillis: Long? = null
    private var stops: List<StopBo> = emptyList()
    private var lastStopsSearchQuery: String = ""
    private var searchSuggestionsJob: Job? = null

    init {
        syncAll()
        observeFavoriteStopsFromLocal()
        observeStopsFromLocal()
    }

    fun refreshNoticesIfNeeded() {
        if (System.currentTimeMillis() - (lastNoticesUpdateMillis ?: 0) > NOTICES_UPDATE_TIME_MILLIS) {
            fetchNotices()
        }
    }

    private fun fetchNotices() {
        viewModelScope.launch(Dispatchers.IO) {
            getNoticesUseCase().collect {
                if (it is AsyncResult.Success) {
                    lastNoticesUpdateMillis = System.currentTimeMillis()
                }
                _noticesLiveData.postValue(it)
            }
        }
    }

    private fun observeFavoriteStopsFromLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            getFavoriteStopsUseCase().collect {
                _favoriteStopsLiveData.postValue(it.toList())
            }
        }
    }

    private fun observeStopsFromLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            getStopsWithLinesFromLocalUseCase().collect {
                stops = it
                filterStopSuggestions(lastStopsSearchQuery)
            }
        }
    }

    fun filterStopSuggestions(searchQuery: String) {
        searchSuggestionsJob?.cancel()
        lastStopsSearchQuery = searchQuery
        searchSuggestionsJob = viewModelScope.launch(Dispatchers.Default) {
            if (searchQuery.isNotBlank()) {
                val filters = searchQuery.normalize().split(" ")
                val filteredList = stops.filter { stop ->
                    ensureActive()
                    val stopCode = stop.code.toString()
                    val normalizedDescription = stop.description?.normalize()
                    filters.all { stopCode.contains(it) || normalizedDescription?.contains(it) == true }
                }
                ensureActive()
                _stopSuggestionsLiveData.postValue(filteredList)
            } else {
                _stopSuggestionsLiveData.postValue(stops.toList())
            }
        }
    }

    private fun syncAll() {
        viewModelScope.launch(Dispatchers.IO) {
            syncAllUseCase()
        }
    }

    private companion object {
        private const val NOTICES_UPDATE_TIME_MILLIS = 300000 // 5 minutes
    }

}