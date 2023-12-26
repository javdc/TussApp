package com.javdc.tussapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.javdc.tussapp.data.local.dao.CardDao
import com.javdc.tussapp.data.local.dao.DateVersionDao
import com.javdc.tussapp.data.local.dao.FavoriteStopDao
import com.javdc.tussapp.data.local.dao.StopDao
import com.javdc.tussapp.data.local.model.CardDbo
import com.javdc.tussapp.data.local.model.DateVersionDbo
import com.javdc.tussapp.data.local.model.FavoriteStopDbo
import com.javdc.tussapp.data.local.model.LineDbo
import com.javdc.tussapp.data.local.model.StopDbo
import com.javdc.tussapp.data.local.model.StopLineCrossReferenceDbo

@Database(entities = [FavoriteStopDbo::class, CardDbo::class, DateVersionDbo::class, StopDbo::class, LineDbo::class, StopLineCrossReferenceDbo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardStatusDao(): CardDao
    abstract fun dateVersionDao(): DateVersionDao
    abstract fun favoriteStopDao(): FavoriteStopDao
    abstract fun stopDao(): StopDao
}