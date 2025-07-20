plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    // kotlin("multiplatform") version "1.9.25"
    // kotlin("plugin.serialization") version "1.9.25"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

// kotlin {
//     jvm()
//     wasmJs {
//         browser()
//     }
//
//     sourceSets {
//         commonMain.dependencies {
//             implementation(libs.kotlinx.coroutines.core)
//             implementation(libs.ktor.serialization.kotlinx.json)
//         }
//
//         commonTest.dependencies {
//             implementation(libs.kotlin.test.junit)
//         }
//     }
// }

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.serialization.kotlinx.json)
    testImplementation(libs.kotlin.test.junit)
}