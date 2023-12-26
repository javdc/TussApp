package com.javdc.tussapp.presentation.ui.estimates

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javdc.tussapp.domain.usecase.AddStopToFavoritesUseCase
import com.javdc.tussapp.domain.usecase.GetStopEstimatesUseCase
import com.javdc.tussapp.domain.usecase.IsStopFavoriteUseCase
import com.javdc.tussapp.domain.usecase.RemoveStopFromFavoritesUseCase
import com.javdc.tussapp.domain.util.AsyncResult
import com.javdc.tussapp.domain.util.map
import com.javdc.tussapp.presentation.mapper.toVo
import com.javdc.tussapp.presentation.model.StopEstimatesInfoVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StopEstimatesViewModel @Inject constructor(
    private val getStopEstimatesUseCase: GetStopEstimatesUseCase,
    private val isStopFavoriteUseCase: IsStopFavoriteUseCase,
    private val addStopToFavoritesUseCase: AddStopToFavoritesUseCase,
    private val removeStopFromFavoritesUseCase: RemoveStopFromFavoritesUseCase,
) : ViewModel() {

    private val _stopEstimatesLiveData = MutableLiveData<AsyncResult<StopEstimatesInfoVo>>()
    val stopEstimatesLiveData: LiveData<AsyncResult<StopEstimatesInfoVo>>
        get() = _stopEstimatesLiveData

    private val _stopFavoriteStatusLiveData = MutableLiveData<Boolean>()
    val stopFavoriteStatusLiveData: LiveData<Boolean>
        get() = _stopFavoriteStatusLiveData

    fun fetchEstimates(stopCode: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getStopEstimatesUseCase(stopCode).collect { result ->
                _stopEstimatesLiveData.postValue(result.map { it.toVo() })
            }
        }
    }

    fun observeFavoriteStatusFromLocal(code: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            isStopFavoriteUseCase(code).collect {
                _stopFavoriteStatusLiveData.postValue(it)
            }
        }
    }

    fun addStopToFavorites(code: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            addStopToFavoritesUseCase(code)
        }
    }

    fun removeStopFromFavorites(code: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            removeStopFromFavoritesUseCase(code)
        }
    }

}