package com.javdc.tussapp.data.remote.datasource

import com.javdc.tussapp.data.remote.api.NoticeService
import com.javdc.tussapp.data.remote.mapper.toBo
import com.javdc.tussapp.domain.model.NoticeBo

interface NoticeRemoteDataSource {
    suspend fun getNotices(): List<NoticeBo>
}

class NoticeRemoteDataSourceImpl(
    private val noticeService: NoticeService
) : NoticeRemoteDataSource {

    override suspend fun getNotices(): List<NoticeBo> =
        noticeService
            .getNotices()
            .toBo()

}