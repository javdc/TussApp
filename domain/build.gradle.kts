import com.javdc.tussapp.AppConfig
import com.javdc.tussapp.dependencies.Versions

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("com.google.devtools.ksp")
}

java {
    sourceCompatibility = AppConfig.javaSourceCompatibility
    targetCompatibility = AppConfig.javaTargetCompatibility
}

kotlin {
    jvmToolchain(AppConfig.kotlinJvmToolchainJdkVersion)
}

dependencies {
    implementation(project(":common"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}")
    implementation("com.google.dagger:hilt-core:${Versions.hiltVersion}")
    ksp("com.google.dagger:hilt-compiler:${Versions.hiltVersion}")
    implementation("co.touchlab:kermit:${Versions.kermitVersion}")

    testImplementation("junit:junit:${Versions.jUnitVersion}")
    testImplementation("io.mockk:mockk:${Versions.mockKVersion}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesVersion}")
}