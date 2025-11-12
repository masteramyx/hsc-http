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