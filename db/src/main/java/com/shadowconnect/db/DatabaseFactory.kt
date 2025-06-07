package com.shadowconnect.db

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.healthshadow.db.HealthShadowDatabase
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

object DatabaseFactory {
    private var database: HealthShadowDatabase? = null
    private var dataSource: HikariDataSource? = null

    fun init(
        databaseUrl: String = "jdbc:postgresql://localhost:5432/healthshadow_dev",
        username: String = "hsc_user",
        password: String = "hsc_dev_password"
    ) {
        val config = HikariConfig().apply {
            jdbcUrl = databaseUrl
            this.username = username
            this.password = password
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 10
            isAutoCommit = false
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