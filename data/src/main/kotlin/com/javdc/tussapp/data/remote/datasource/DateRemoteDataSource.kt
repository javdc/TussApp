package com.javdc.tussapp.data.remote.datasource

import com.javdc.tussapp.data.remote.api.DateService
import com.javdc.tussapp.data.remote.mapper.toBo
import com.javdc.tussapp.domain.model.DateVersionBo

interface DateRemoteDataSource {
    suspend fun getDates(): List<DateVersionBo>
}

class DateRemoteDataSourceImpl(
    private val dateService: DateService
) : DateRemoteDataSource {

    override suspend fun getDates(): List<DateVersionBo> =
        dateService
            .getDates()
            .toBo()

}