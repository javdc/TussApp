package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.repository.CardRepository

interface RemoveCardFromLocalUseCase {
    suspend operator fun invoke(code: Long)
}

class RemoveCardFromLocalUseCaseImpl(private val repository: CardRepository) : RemoveCardFromLocalUseCase {
    override suspend operator fun invoke(code: Long) {
        return repository.removeCardFromLocal(code)
    }
}