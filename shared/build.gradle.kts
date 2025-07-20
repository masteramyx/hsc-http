plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

kotlin {
    jvm()
    js(IR) {
        browser()
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.serialization.kotlinx.json)
        }
        
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}