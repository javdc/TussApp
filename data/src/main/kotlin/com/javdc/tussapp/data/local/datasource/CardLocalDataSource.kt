package com.javdc.tussapp.data.local.datasource

import com.javdc.tussapp.data.local.dao.CardDao
import com.javdc.tussapp.data.local.mapper.toBo
import com.javdc.tussapp.data.local.mapper.toDbo
import com.javdc.tussapp.data.local.mapper.toUpdateFromRemoteDbo
import com.javdc.tussapp.domain.model.CardBo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CardLocalDataSource {
    suspend fun getCards(): Flow<List<CardBo>>
    suspend fun getCardNumbers(): List<Long>
    suspend fun addCard(card: CardBo)
    suspend fun addCards(cards: List<CardBo>)
    suspend fun upsertCardFromRemote(card: CardBo)
    suspend fun upsertCardsFromRemote(cards: List<CardBo>)
    suspend fun removeCardStatus(cardNumber: Long)
}

class CardLocalDataSourceImpl(private val cardDao: CardDao) : CardLocalDataSource {

    override suspend fun getCards(): Flow<List<CardBo>> =
        cardDao.getCards().map { list -> list.map { it.toBo() } }

    override suspend fun getCardNumbers(): List<Long> =
        cardDao.getCardNumbers()

    override suspend fun addCard(card: CardBo) =
        cardDao.addCard(card.toDbo())

    override suspend fun addCards(cards: List<CardBo>) =
        cardDao.addCards(cards.map { it.toDbo() })

    override suspend fun upsertCardFromRemote(card: CardBo) =
        cardDao.upsertCardFromRemote(card.toUpdateFromRemoteDbo())

    override suspend fun upsertCardsFromRemote(cards: List<CardBo>) =
        cardDao.upsertCardsFromRemote(cards.map { it.toUpdateFromRemoteDbo() })

    override suspend fun removeCardStatus(cardNumber: Long) =
        cardDao.removeCard(cardNumber)

}