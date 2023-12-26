package com.javdc.tussapp.domain.usecase

import com.javdc.tussapp.domain.model.NoticeBo
import com.javdc.tussapp.domain.repository.NoticeRepository
import com.javdc.tussapp.domain.util.AsyncResult
import kotlinx.coroutines.flow.Flow

interface GetNoticesUseCase {
    suspend operator fun invoke(): Flow<AsyncResult<List<NoticeBo>>>
}

class GetNoticesUseCaseImpl(private val repository: NoticeRepository) : GetNoticesUseCase {
    override suspend operator fun invoke(): Flow<AsyncResult<List<NoticeBo>>> {
        return repository.getNotices()
    }

}