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
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.3")
    }

}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

plugins {
    kotlin("jvm") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21"
}

group = "com.shadowconnect"
version = "0.0.1"