package com.javdc.tussapp.domain.model

data class CardBo(
    val cardNumber: Long,
    val customName: String?,
    val serialNumber: Long,
    val cardType: CardTypeBo?,
    val passName: String?,
    val lastOperation: String?,
    val validFrom: String?,
    val validTo: String?,
    val extensionFrom: String?,
    val extensionTo: String?,
    val eurosBalance: Float?,
    val tripsBalance: Int?,
    val resultCode: Int?,
    val alertCode: Int?,
    val expiry: String?,
)