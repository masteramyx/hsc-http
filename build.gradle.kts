val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val mockk_version: String by project
val sql_delight_version: String by project

buildscript {
    repositories {
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
    application
    kotlin("jvm") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21"
}

group = "com.shadowconnect"
version = "0.0.1"

//application {
//    mainClass.set("com.shadowconnect.ApplicationKt")
//
//    val isDevelopment: Boolean = project.ext.has("development")
//    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
//}

tasks {
    create("stage").dependsOn("build", "installDist")
}

dependencies {
//    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
//    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
//    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
//    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
//    implementation("ch.qos.logback:logback-classic:$logback_version")
//    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
//    testImplementation("io.ktor:ktor-client-mock:$ktor_version")
//    testImplementation("io.mockk:mockk:$mockk_version")
}