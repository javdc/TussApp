package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.repository.CardRepository
import com.javdc.tussapp.domain.util.AsyncError
import com.javdc.tussapp.domain.util.AsyncResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

interface GetCardAndSaveInLocalUseCase {
    suspend operator fun invoke(cardNumber: Long, customName: String?): Flow<AsyncResult<Unit>>
}

class GetCardAndSaveInLocalUseCaseImpl(
    private val repository: CardRepository,
    private val getCardStatusUseCase: GetCardStatusUseCase,
) : GetCardAndSaveInLocalUseCase {

    override suspend fun invoke(cardNumber: Long, customName: String?): Flow<AsyncResult<Unit>> {
        return flow {
            emit(AsyncResult.Loading())
            val result = getCardStatusUseCase(cardNumber).last()
            if (result is AsyncResult.Success) {
                repository.saveCardInLocal(result.data.copy(customName = customName))
                emit(AsyncResult.Success(Unit))
            } else {
                emit(AsyncResult.Error((result as? AsyncResult.Error)?.error ?: AsyncError.UnknownError()))
            }
        }
    }

}