package com.javdc.tussapp.data.repository

import com.javdc.tussapp.data.local.datasource.DateLocalDataSource
import com.javdc.tussapp.data.remote.datasource.DateRemoteDataSource
import com.javdc.tussapp.data.repository.util.RepositoryErrorManager
import com.javdc.tussapp.domain.model.DateVersionBo
import com.javdc.tussapp.domain.repository.DateRepository
import com.javdc.tussapp.domain.util.AsyncError
import com.javdc.tussapp.domain.util.AsyncResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import java.time.LocalDate

class DateRepositoryImpl(
    private val dateRemoteDataSource: DateRemoteDataSource,
    private val dateLocalDataSource: DateLocalDataSource,
): DateRepository {

    override suspend fun getDateVersions(): Flow<AsyncResult<List<DateVersionBo>>> =
        RepositoryErrorManager.wrap {
            dateRemoteDataSource.getDates()
        }

    override suspend fun getDateVersionsFromLocal(): Flow<List<DateVersionBo>> =
        dateLocalDataSource.getDateVersionsFlow()

    override suspend fun syncDateVersionsInLocal(): AsyncResult<Unit> {
        val result = getDateVersions().last()
        return if (result is AsyncResult.Success) {
            dateLocalDataSource.deleteAllAndInsertDateVersions(result.data)
            AsyncResult.Success(Unit)
        } else {
            AsyncResult.Error((result as? AsyncResult.Error)?.error ?: AsyncError.UnknownError())
        }
    }

    override suspend fun getVersion(date: LocalDate): Int? {
        return dateLocalDataSource.getVersion(date)
    }

}