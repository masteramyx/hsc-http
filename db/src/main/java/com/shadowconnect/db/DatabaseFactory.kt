package com.shadowconnect.db

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.healthshadow.db.HealthShadowDatabase
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

object DatabaseFactory {
    private var database: HealthShadowDatabase? = null
    private var dataSource: HikariDataSource? = null

    fun init(
        databaseUrl: String = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/healthshadow_dev",
        username: String = System.getenv("DATABASE_USER") ?: "hsc_user",
        password: String = System.getenv("DATABASE_PASSWORD") ?: "hsc_dev_password"
    ) {
        // Parse Render's DATABASE_URL format: postgresql://user:pass@host/dbname (no port)
        // Convert to JDBC format: jdbc:postgresql://host:5432/dbname
        val (jdbcUrl, dbUser, dbPassword) = if (databaseUrl.startsWith("postgresql://") || databaseUrl.startsWith("postgres://")) {
            // Extract: postgresql://user:pass@host/database
            val withoutProtocol = databaseUrl.substringAfter("://")
            val credentials = withoutProtocol.substringBefore("@")
            val hostAndDb = withoutProtocol.substringAfter("@")

            val user = credentials.substringBefore(":")
            val pass = credentials.substringAfter(":")

            // Add port 5432 if not present
            val jdbcHost = if (hostAndDb.contains(":")) hostAndDb else {
                val host = hostAndDb.substringBefore("/")
                val db = hostAndDb.substringAfter("/")
                "$host:5432/$db"
            }

            Triple("jdbc:postgresql://$jdbcHost", user, pass)
        } else {
            Triple(databaseUrl, username, password)
        }

        val config = HikariConfig().apply {
            this.jdbcUrl = jdbcUrl
            this.username = dbUser
            this.password = dbPassword
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 10
            isAutoCommit = true
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        
        dataSource = HikariDataSource(config)
        val driver = dataSource?.asJdbcDriver() ?: throw IllegalStateException("Failed to create DataSource")

        // Create tables if they don't exist
        HealthShadowDatabase.Schema.create(driver)

        database = HealthShadowDatabase(driver)
    }

    fun getDatabase(): HealthShadowDatabase {
        return database ?: throw IllegalStateException(
            "Database not initialized. Call DatabaseFactory.init() first."
        )
    }

    fun close() {
        dataSource?.close()
        database = null
        dataSource = null
    }
}