package com.javdc.tussapp.domain.repository

import com.javdc.tussapp.domain.model.CardBo
import com.javdc.tussapp.domain.util.AsyncResult
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    suspend fun getCardStatus(cardNumber: Long): Flow<AsyncResult<CardBo>>
    suspend fun getCardsFromLocal(): Flow<List<CardBo>>
    suspend fun saveCardInLocal(card: CardBo)
    suspend fun saveCardsInLocal(cards: List<CardBo>)
    suspend fun updateCardFromRemoteInLocal(card: CardBo)
    suspend fun updateCardsFromRemoteInLocal(cards: List<CardBo>)
    suspend fun removeCardFromLocal(cardNumber: Long)
    suspend fun syncCardsInLocal(): AsyncResult<Unit>
}