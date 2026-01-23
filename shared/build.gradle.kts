plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

android {
    namespace = "com.shadowconnects.shared"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin {
    jvm()
    js(IR) {
        browser()
        binaries.library()
        useEsModules()  // Generate ES modules instead of UMD

        compilations.all {
            kotlinOptions {
                moduleKind = "es"
                sourceMap = true
                sourceMapEmbedSources = "always"
            }
        }

        generateTypeScriptDefinitions()
    }
    androidTarget()

    sourceSets {

        androidMain.dependencies {
            implementation(libs.kotlin.stdlib)
        }

        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.serialization.kotlinx.json)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}