package com.javdc.tussapp.data.repository

import com.javdc.tussapp.data.local.datasource.CardLocalDataSource
import com.javdc.tussapp.data.remote.datasource.CardRemoteDataSource
import com.javdc.tussapp.data.repository.util.RepositoryErrorManager
import com.javdc.tussapp.domain.model.CardBo
import com.javdc.tussapp.domain.repository.CardRepository
import com.javdc.tussapp.domain.util.AsyncResult
import com.javdc.tussapp.domain.util.mapToSingleAsyncResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last

class CardRepositoryImpl(
    private val cardRemoteDataSource: CardRemoteDataSource,
    private val cardLocalDataSource: CardLocalDataSource,
): CardRepository {

    override suspend fun getCardStatus(cardNumber: Long): Flow<AsyncResult<CardBo>> =
        RepositoryErrorManager.wrap {
            cardRemoteDataSource.getCardStatus(cardNumber)
        }

    override suspend fun getCardsFromLocal(): Flow<List<CardBo>> =
        cardLocalDataSource.getCards()

    override suspend fun saveCardInLocal(card: CardBo) =
        cardLocalDataSource.addCard(card)

    override suspend fun saveCardsInLocal(cards: List<CardBo>) =
        cardLocalDataSource.addCards(cards)

    override suspend fun updateCardFromRemoteInLocal(card: CardBo) =
        cardLocalDataSource.upsertCardFromRemote(card)

    override suspend fun updateCardsFromRemoteInLocal(cards: List<CardBo>) =
        cardLocalDataSource.upsertCardsFromRemote(cards)

    override suspend fun removeCardFromLocal(cardNumber: Long) =
        cardLocalDataSource.removeCardStatus(cardNumber)

    override suspend fun syncCardsInLocal(): AsyncResult<Unit> {
        return coroutineScope {
            val results = cardLocalDataSource.getCardNumbers().map { cardNumber ->
                async {
                    ensureActive()
                    getCardStatus(cardNumber).last()
                }
            }.awaitAll()

            ensureActive()
            val updatedCards = results.filterIsInstance<AsyncResult.Success<CardBo>>().map { it.data }
            updateCardsFromRemoteInLocal(updatedCards)

            return@coroutineScope results.mapToSingleAsyncResult()
        }
    }

}