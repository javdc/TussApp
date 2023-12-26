import com.javdc.tussapp.AppConfig
import com.javdc.tussapp.dependencies.Versions

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("androidx.room")
}

android {
    namespace = "${AppConfig.applicationId}.data"
    compileSdk = AppConfig.compileSdkVersion

    defaultConfig {
        minSdk = AppConfig.minSdkVersion

        testInstrumentationRunner = AppConfig.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}")
    implementation("com.google.dagger:hilt-android:${Versions.hiltVersion}")
    ksp("com.google.dagger:hilt-compiler:${Versions.hiltVersion}")
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-gson:${Versions.retrofitVersion}")
    implementation("com.squareup.okhttp3:okhttp:${Versions.okHttpVersion}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Versions.okHttpVersion}")
    implementation("com.google.code.gson:gson:${Versions.gsonVersion}")
    implementation("androidx.room:room-runtime:${Versions.roomVersion}")
    ksp("androidx.room:room-compiler:${Versions.roomVersion}")
    implementation("androidx.room:room-ktx:${Versions.roomVersion}")
    implementation("co.touchlab:kermit:${Versions.kermitVersion}")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:${Versions.desugarJdkLibsVersion}")

    testImplementation("junit:junit:${Versions.jUnitVersion}")
    androidTestImplementation("androidx.test.ext:junit:${Versions.androidXTestJunitVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.androidXTestEspressoVersion}")
}