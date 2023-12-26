package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.model.CardBo
import com.javdc.tussapp.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow

interface GetCardsFromLocalUseCase {
    suspend operator fun invoke(): Flow<List<CardBo>>
}

class GetCardsFromLocalUseCaseImpl(private val repository: CardRepository) : GetCardsFromLocalUseCase {
    override suspend operator fun invoke(): Flow<List<CardBo>> {
        return repository.getCardsFromLocal()
    }

}