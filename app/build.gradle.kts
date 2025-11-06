plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

application {
    mainClass.set("com.shadowconnect.ApplicationKt")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

group = "com.shadowconnect"
version = "0.0.1"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

tasks {
    create("stage").dependsOn("build", "installDist")
}
// build the project before we aggregate all the files for distribution.
tasks.getByName("installDist").mustRunAfter("build")

dependencies {
    implementation(project(":core"))
    implementation(project(":db"))
    implementation(project(":shared"))
    implementation(libs.kotlin.stdlib)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.sessions)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.html.builder)
    implementation(libs.bcrypt)
    implementation(libs.logback.classic)

    // Ktor Client for Claude API
    implementation("io.ktor:ktor-client-core:2.3.12")
    implementation("io.ktor:ktor-client-cio:2.3.12")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.12")

    // AWS SDK for R2 Storage
    implementation(libs.aws.s3)

    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.mockk)
    testImplementation(libs.junit.jupiter.api.app)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}