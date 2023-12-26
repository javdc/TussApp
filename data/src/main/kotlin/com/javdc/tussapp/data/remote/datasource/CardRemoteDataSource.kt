package com.javdc.tussapp.data.remote.datasource

import com.javdc.tussapp.data.remote.api.CardService
import com.javdc.tussapp.data.remote.mapper.toBo
import com.javdc.tussapp.domain.model.CardBo

interface CardRemoteDataSource {
    suspend fun getCardStatus(cardNumber: Long): CardBo
}

class CardRemoteDataSourceImpl(
    private val cardService: CardService
) : CardRemoteDataSource {

    override suspend fun getCardStatus(cardNumber: Long): CardBo =
        cardService
            .getCardStatus(cardNumber)
            .toBo(cardNumber)

}