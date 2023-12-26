package com.javdc.tussapp.common.util

fun String.takeIfNotBlank() = takeIf { it.isNotBlank() }