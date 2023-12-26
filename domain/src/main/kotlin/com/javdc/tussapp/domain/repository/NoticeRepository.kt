package com.javdc.tussapp.domain.repository

import com.javdc.tussapp.domain.model.NoticeBo
import com.javdc.tussapp.domain.util.AsyncResult
import kotlinx.coroutines.flow.Flow

interface NoticeRepository {
    suspend fun getNotices(): Flow<AsyncResult<List<NoticeBo>>>
}