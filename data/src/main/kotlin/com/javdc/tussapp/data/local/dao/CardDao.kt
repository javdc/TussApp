package com.javdc.tussapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.javdc.tussapp.data.local.model.CardDbo
import com.javdc.tussapp.data.local.model.CardUpdateFromRemoteDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Query("SELECT * FROM card")
    fun getCards(): Flow<List<CardDbo>>

    @Query("SELECT card_number FROM card")
    suspend fun getCardNumbers(): List<Long>

    @Upsert
    suspend fun addCard(card: CardDbo)

    @Upsert
    suspend fun addCards(cards: List<CardDbo>)

    @Upsert(entity = CardDbo::class)
    suspend fun upsertCardFromRemote(card: CardUpdateFromRemoteDbo)

    @Upsert(entity = CardDbo::class)
    suspend fun upsertCardsFromRemote(cards: List<CardUpdateFromRemoteDbo>)

    @Query("DELETE FROM card where card_number = :cardNumber")
    suspend fun removeCard(cardNumber: Long)

}