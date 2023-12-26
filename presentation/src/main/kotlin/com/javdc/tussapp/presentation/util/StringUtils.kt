package com.javdc.tussapp.presentation.util

import java.text.Normalizer
import java.text.NumberFormat
import java.util.Currency

fun Float.toFormattedEurosString(): String {
    val numberFormat = NumberFormat.getCurrencyInstance().apply {
        currency = Currency.getInstance("EUR")
    }
    return numberFormat.format(this)
}

private val nonSpacingCombiningCharactersRegex = Regex("\\p{Mn}+")

fun String.normalize() =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(nonSpacingCombiningCharactersRegex, "")
        .lowercase()

fun Long.formatCardNumberToString() =
    buildString {
        this@formatCardNumberToString.toString().mapIndexed { index, character ->
            if (index != 0 && index % 4 == 0) {
                append(" ")
            }
            append(character)
        }
    }