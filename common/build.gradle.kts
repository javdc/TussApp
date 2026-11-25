import com.javdc.tussapp.AppConfig
import com.javdc.tussapp.dependencies.Versions

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("kotlin")
}

java {
    sourceCompatibility = AppConfig.javaSourceCompatibility
    targetCompatibility = AppConfig.javaTargetCompatibility
}

// Don't require a JDK 8 toolchain installed: compile with the JDK running Gradle
// (e.g. the one bundled with Android Studio) and just target Java 8 bytecode.
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = AppConfig.kotlinJvmTarget
}

dependencies {
    implementation("co.touchlab:kermit:${Versions.kermitVersion}")
}