import com.javdc.tussapp.AppConfig
import com.javdc.tussapp.dependencies.Versions

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
}

android {
    namespace = "${AppConfig.applicationId}.presentation"
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
        viewBinding = true
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain"))

    implementation("androidx.core:core-ktx:${Versions.coreKtxVersion}")
    implementation("androidx.appcompat:appcompat:${Versions.appCompatVersion}")
    implementation("com.google.android.material:material:${Versions.materialVersion}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayoutVersion}")
    implementation("androidx.browser:browser:${Versions.browserVersion}")
    implementation("com.google.dagger:hilt-android:${Versions.hiltVersion}")
    implementation("androidx.hilt:hilt-navigation-fragment:${Versions.hiltNavigationFragmentVersion}")
    ksp("com.google.dagger:hilt-compiler:${Versions.hiltVersion}")
    implementation("androidx.databinding:viewbinding:${Versions.viewBindingVersion}")
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.navigationVersion}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.navigationVersion}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensionsVersion}")
    implementation("com.github.bumptech.glide:glide:${Versions.glideVersion}")
    implementation("co.touchlab:kermit:${Versions.kermitVersion}")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:${Versions.desugarJdkLibsVersion}")

    testImplementation("junit:junit:${Versions.jUnitVersion}")
    androidTestImplementation("androidx.test.ext:junit:${Versions.androidXTestJunitVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.androidXTestEspressoVersion}")
}