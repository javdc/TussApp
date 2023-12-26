package com.javdc.tussapp.data.local.datasource

import com.javdc.tussapp.data.local.dao.DateVersionDao
import com.javdc.tussapp.data.local.mapper.toBo
import com.javdc.tussapp.data.local.mapper.toDbo
import com.javdc.tussapp.data.local.model.DateVersionDbo
import com.javdc.tussapp.domain.model.DateVersionBo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

interface DateLocalDataSource {
    suspend fun getDateVersions(): List<DateVersionBo>
    fun getDateVersionsFlow(): Flow<List<DateVersionBo>>
    suspend fun deleteAllAndInsertDateVersions(dateVersions: List<DateVersionBo>)
    suspend fun getDateVersion(date: LocalDate): DateVersionDbo?
    suspend fun getVersion(date: LocalDate): Int?
}

class DateLocalDataSourceImpl(
    private val dateVersionDao: DateVersionDao,
) : DateLocalDataSource {

    override suspend fun getDateVersions(): List<DateVersionBo> =
        dateVersionDao.getDateVersions().map { it.toBo() }

    override fun getDateVersionsFlow(): Flow<List<DateVersionBo>> =
        dateVersionDao.getDateVersionsFlow().map { list -> list.map { it.toBo() } }

    override suspend fun deleteAllAndInsertDateVersions(dateVersions: List<DateVersionBo>) =
        dateVersionDao.insert(dateVersions.map {it.toDbo() })

    override suspend fun getDateVersion(date: LocalDate) =
        dateVersionDao.getDateVersion(date.toString())

    override suspend fun getVersion(date: LocalDate) =
        dateVersionDao.getVersion(date.toString())

}