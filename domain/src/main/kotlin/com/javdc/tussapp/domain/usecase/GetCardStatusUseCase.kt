package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.model.CardBo
import com.javdc.tussapp.domain.repository.CardRepository
import com.javdc.tussapp.domain.util.AsyncResult
import kotlinx.coroutines.flow.Flow

interface GetCardStatusUseCase {
    suspend operator fun invoke(cardNumber: Long): Flow<AsyncResult<CardBo>>
}

class GetCardStatusUseCaseImpl(private val repository: CardRepository) : GetCardStatusUseCase {
    override suspend operator fun invoke(cardNumber: Long): Flow<AsyncResult<CardBo>> {
        return repository.getCardStatus(cardNumber)
    }

}