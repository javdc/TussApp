package com.javdc.tussapp

import org.gradle.api.JavaVersion

object AppConfig {
    const val applicationId = "com.javdc.tussapp"
    const val versionCode = 1
    const val versionName = "0.1.0"
    const val compileSdkVersion = 34
    const val minSdkVersion = 21
    const val targetSdkVersion = 34
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    val javaSourceCompatibility = JavaVersion.VERSION_1_8
    val javaTargetCompatibility = JavaVersion.VERSION_1_8
    const val kotlinJvmTarget = "1.8"
    const val kotlinJvmToolchainJdkVersion = 8
}