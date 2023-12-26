package com.javdc.tussapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "stop")
data class StopDbo(
    @PrimaryKey @ColumnInfo(name = "stop_code") val code: Int,
    @ColumnInfo(name = "description") val description: String?,
)

@Entity(tableName = "line")
data class LineDbo(
    @PrimaryKey @ColumnInfo(name = "line_id") val id: Int,
    @ColumnInfo(name = "label") val label: String?,
    @ColumnInfo(name = "color") val color: String?,
)

@Entity(tableName = "stop_line_cross_ref", primaryKeys = ["stop_code", "line_id"])
data class StopLineCrossReferenceDbo(
    @ColumnInfo(name = "stop_code") val stopCode: Int,
    @ColumnInfo(name = "line_id", index = true) val lineId: Int,
)

data class StopWithLinesDbo(
    @Embedded val stop: StopDbo,
    @Relation(
        parentColumn = "stop_code",
        entityColumn = "line_id",
        associateBy = Junction(StopLineCrossReferenceDbo::class),
    )
    val lines: List<LineDbo>,
)

@Entity(tableName = "favorite_stop")
data class FavoriteStopDbo(
    @PrimaryKey @ColumnInfo(name = "stop_code") val code: Int,
)