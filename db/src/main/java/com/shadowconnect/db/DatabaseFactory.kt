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
        // Parse Render's DATABASE_URL format: postgres://user:pass@host:port/dbname
        // Convert to JDBC format: jdbc:postgresql://host:port/dbname
        val jdbcUrl = if (databaseUrl.startsWith("postgres://") && !databaseUrl.startsWith("jdbc:")) {
            databaseUrl.replaceFirst("postgres://", "jdbc:postgresql://")
        } else {
            databaseUrl
        }

        val config = HikariConfig().apply {
            this.jdbcUrl = jdbcUrl
            this.username = username
            this.password = password
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 10
            isAutoCommit = true
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        
        dataSource = HikariDataSource(config)
        val driver = dataSource?.asJdbcDriver() ?: throw IllegalStateException("Failed to create DataSource")
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