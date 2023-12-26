package com.javdc.tussapp.common.util

fun <T> Iterable<T>.areAllTheSame(): Boolean {
    return this.distinct().size == 1
}