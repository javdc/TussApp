import com.javdc.tussapp.AppConfig
import com.javdc.tussapp.dependencies.Versions

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = AppConfig.applicationId
    compileSdk = AppConfig.compileSdkVersion

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".dev"
        }
    }

    compileOptions {
        sourceCompatibility = AppConfig.javaSourceCompatibility
        targetCompatibility = AppConfig.javaTargetCompatibility
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = AppConfig.kotlinJvmTarget
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))

    implementation("com.google.android.material:material:${Versions.materialVersion}")
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Versions.leakCanaryVersion}")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:${Versions.desugarJdkLibsVersion}")

    implementation("com.google.dagger:hilt-android:${Versions.hiltVersion}")
    ksp("com.google.dagger:hilt-compiler:${Versions.hiltVersion}")
}
