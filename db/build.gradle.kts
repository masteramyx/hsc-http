plugins {
    kotlin("jvm")
    java
    id("com.squareup.sqldelight")
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
    this.database("HealthShadowDatabase") {
        packageName = "com.healthshadow.db"
        dialect = "postgresql"
        sourceFolders = listOf("sql", "java/com/shadowconnect/db/sql")
    }
}

group = "com.shadowconnect"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup.sqldelight:jdbc-driver:1.5.0")
    api("com.squareup.sqldelight:runtime:$1.5.3")
    api("com.squareup.sqldelight:jdbc-driver:$1.5.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
