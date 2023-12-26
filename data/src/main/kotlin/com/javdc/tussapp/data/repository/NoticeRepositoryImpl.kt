package com.javdc.tussapp.data.repository

import com.javdc.tussapp.data.remote.datasource.NoticeRemoteDataSource
import com.javdc.tussapp.data.repository.util.RepositoryErrorManager
import com.javdc.tussapp.domain.model.NoticeBo
import com.javdc.tussapp.domain.repository.NoticeRepository
import com.javdc.tussapp.domain.util.AsyncResult
import kotlinx.coroutines.flow.Flow

class NoticeRepositoryImpl(
    private val noticeRemoteDataSource: NoticeRemoteDataSource,
): NoticeRepository {

    override suspend fun getNotices(): Flow<AsyncResult<List<NoticeBo>>> =
        RepositoryErrorManager.wrap {
            noticeRemoteDataSource.getNotices()
        }

}