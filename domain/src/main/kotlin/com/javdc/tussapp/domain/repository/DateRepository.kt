package com.javdc.tussapp.domain.repository

import com.javdc.tussapp.domain.model.DateVersionBo
import com.javdc.tussapp.domain.util.AsyncResult
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface DateRepository {
    suspend fun getDateVersions(): Flow<AsyncResult<List<DateVersionBo>>>
    suspend fun getDateVersionsFromLocal(): Flow<List<DateVersionBo>>
    suspend fun syncDateVersionsInLocal(): AsyncResult<Unit>
    suspend fun getVersion(date: LocalDate): Int?
}