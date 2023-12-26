package com.javdc.tussapp.presentation.ui.cards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javdc.tussapp.domain.usecase.GetCardAndSaveInLocalUseCase
import com.javdc.tussapp.domain.usecase.GetCardsFromLocalUseCase
import com.javdc.tussapp.domain.usecase.RemoveCardFromLocalUseCase
import com.javdc.tussapp.domain.usecase.SyncCardsUseCase
import com.javdc.tussapp.domain.util.AsyncResult
import com.javdc.tussapp.presentation.mapper.toVo
import com.javdc.tussapp.presentation.model.CardVo
import com.javdc.tussapp.presentation.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val getCardsFromLocalUseCase: GetCardsFromLocalUseCase,
    private val syncCardsUseCase: SyncCardsUseCase,
    private val getCardAndSaveInLocalUseCase: GetCardAndSaveInLocalUseCase,
    private val removeCardFromLocalUseCase: RemoveCardFromLocalUseCase
) : ViewModel() {

    private val _cardsLiveData = MutableLiveData<List<CardVo>>()
    val cardsLiveData: LiveData<List<CardVo>>
        get() = _cardsLiveData

    private val _cardsSyncStatusLiveData = MutableLiveData<AsyncResult<Unit>>()
    val cardsSyncStatusLiveData: LiveData<AsyncResult<Unit>>
        get() = _cardsSyncStatusLiveData

    private val _addCardLiveData = MutableLiveData<Event<AsyncResult<Unit>>>()
    val addCardLiveData: LiveData<Event<AsyncResult<Unit>>>
        get() = _addCardLiveData

    private val _successfullyAddedCardNumberLiveData = MutableLiveData<Event<Long?>>()
    val successfullyAddedCardNumberLiveData: LiveData<Event<Long?>>
        get() = _successfullyAddedCardNumberLiveData

    init {
        observeCardsFromLocal()
        syncCards()
    }

    private fun observeCardsFromLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            getCardsFromLocalUseCase().collect { cards ->
                _cardsLiveData.postValue(cards.map { it.toVo() })
            }
        }
    }

    fun syncCards() {
        viewModelScope.launch(Dispatchers.IO) {
            _cardsSyncStatusLiveData.postValue(AsyncResult.Loading())
            val syncCardsResult = syncCardsUseCase()
            _cardsSyncStatusLiveData.postValue(syncCardsResult)
        }
    }

    fun addCardToLocal(cardNumber: Long, customName: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            getCardAndSaveInLocalUseCase(cardNumber, customName).collect { result ->
                _addCardLiveData.postValue(Event(result))
                _successfullyAddedCardNumberLiveData.postValue(Event(cardNumber.takeIf { result is AsyncResult.Success }))
            }
        }
    }

    fun removeCardStatusFromLocal(cardNumber: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            removeCardFromLocalUseCase(cardNumber)
        }
    }

}