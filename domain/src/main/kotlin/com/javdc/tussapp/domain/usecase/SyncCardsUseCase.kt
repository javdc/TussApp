package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.repository.CardRepository
import com.javdc.tussapp.domain.util.AsyncResult

interface SyncCardsUseCase {
    suspend operator fun invoke(): AsyncResult<Unit>
}

class SyncCardsUseCaseImpl(
    private val repository: CardRepository,
) : SyncCardsUseCase {

    override suspend operator fun invoke(): AsyncResult<Unit> {
        return repository.syncCardsInLocal()
    }

}