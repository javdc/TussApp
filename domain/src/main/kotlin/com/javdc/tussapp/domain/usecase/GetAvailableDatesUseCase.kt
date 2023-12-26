package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.repository.DateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

interface GetAvailableDatesUseCase {
    suspend operator fun invoke(): Flow<List<LocalDate>>
}

class GetAvailableDatesUseCaseImpl(private val dateRepository: DateRepository) : GetAvailableDatesUseCase {
    override suspend operator fun invoke(): Flow<List<LocalDate>> {
        return dateRepository.getDateVersionsFromLocal().map { list -> list.map { it.date } }
    }

}
