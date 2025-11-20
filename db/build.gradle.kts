plugins {
    alias(libs.plugins.kotlin.jvm)
    java
    alias(libs.plugins.sqldelight)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}


sourceSets {
    // This is because SQLDelight finds resources in `src/main` path.
    named("main") {
        java.srcDirs("src/main/kotlin", "src/test/kotlin", "src/main/java")
        resources.srcDirs("src/main/resources", "src/test/resources")
    }
}

// Database Configurations
sqldelight {
    databases {
        create("HealthShadowDatabase") {
            packageName.set("com.healthshadow.db")
            dialect("app.cash.sqldelight:postgresql-dialect:2.0.2")
        }
    }
}

group = "com.shadowconnect"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.kotlin.stdlib)
    implementation(libs.sqldelight.jdbc.driver)
    api(libs.sqldelight.runtime)
    api(libs.sqldelight.jdbc.driver)
    implementation(libs.postgresql.driver)
    implementation(libs.hikaricp)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit.jupiter.api.db)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
