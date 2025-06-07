plugins {
    kotlin("jvm")
    java
    id("app.cash.sqldelight")
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
    implementation(kotlin("stdlib"))
    implementation("app.cash.sqldelight:jdbc-driver:2.0.2")
    api("app.cash.sqldelight:runtime:2.0.2")
    api("app.cash.sqldelight:jdbc-driver:2.0.2")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("com.zaxxer:HikariCP:5.1.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
