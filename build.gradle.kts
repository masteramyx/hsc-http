val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val mockk_version: String by project
val sql_delight_version: String by project

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    }
    dependencies {
        classpath("app.cash.sqldelight:gradle-plugin:2.0.2")
    }

}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

plugins {
    kotlin("jvm") version "1.9.25"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.25"
}

group = "com.shadowconnect"
version = "0.0.1"