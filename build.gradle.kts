

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

group = "com.shadowconnect"
version = "0.0.1"