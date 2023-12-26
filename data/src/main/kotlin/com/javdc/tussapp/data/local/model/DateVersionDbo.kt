package com.javdc.tussapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "date_version")
data class DateVersionDbo(
    @PrimaryKey @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "version") val version: Int,
)