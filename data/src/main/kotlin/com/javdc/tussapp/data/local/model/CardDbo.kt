package com.javdc.tussapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card")
data class CardDbo(
    @PrimaryKey @ColumnInfo(name = "card_number") val cardNumber: Long,
    @ColumnInfo(name = "custom_name") val customName: String?,
    @ColumnInfo(name = "serial_number") val serialNumber: Long,
    @ColumnInfo(name = "pass_code") val passCode: Int?,
    @ColumnInfo(name = "pass_name") val passName: String?,
    @ColumnInfo(name = "last_operation") val lastOperation: String?,
    @ColumnInfo(name = "valid_from") val validFrom: String?,
    @ColumnInfo(name = "valid_to") val validTo: String?,
    @ColumnInfo(name = "extension_from") val extensionFrom: String?,
    @ColumnInfo(name = "extension_to") val extensionTo: String?,
    @ColumnInfo(name = "euros_balance") val eurosBalance: Float?,
    @ColumnInfo(name = "trips_balance") val tripsBalance: Int?,
    @ColumnInfo(name = "result_code") val resultCode: Int?,
    @ColumnInfo(name = "alert_code") val alertCode: Int?,
    @ColumnInfo(name = "expiry") val expiry: String?,
)

data class CardUpdateFromRemoteDbo(
    @PrimaryKey @ColumnInfo(name = "card_number") val cardNumber: Long,
    @ColumnInfo(name = "serial_number") val serialNumber: Long,
    @ColumnInfo(name = "pass_code") val passCode: Int?,
    @ColumnInfo(name = "pass_name") val passName: String?,
    @ColumnInfo(name = "last_operation") val lastOperation: String?,
    @ColumnInfo(name = "valid_from") val validFrom: String?,
    @ColumnInfo(name = "valid_to") val validTo: String?,
    @ColumnInfo(name = "extension_from") val extensionFrom: String?,
    @ColumnInfo(name = "extension_to") val extensionTo: String?,
    @ColumnInfo(name = "euros_balance") val eurosBalance: Float?,
    @ColumnInfo(name = "trips_balance") val tripsBalance: Int?,
    @ColumnInfo(name = "result_code") val resultCode: Int?,
    @ColumnInfo(name = "alert_code") val alertCode: Int?,
    @ColumnInfo(name = "expiry") val expiry: String?,
)