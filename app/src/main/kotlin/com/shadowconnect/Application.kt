package com.shadowconnect

import com.shadowconnect.db.DatabaseFactory
import com.shadowconnect.plugins.*
import com.shadowconnect.utils.logger
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.Charsets
import io.ktor.client.plugins.logging.*
import io.ktor.server.application.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    logger.info("Starting HSC-HTTP application")
    logger.info("PORT environment variable: ${System.getenv("PORT")}")
    
    // Initialize database connection
    DatabaseFactory.init()
    logger.info("Database initialized successfully")
    
    configureSessions()
    configureAuthentication()
    configureCORS()
    configureRouting()
    configureSerialization()
    
    logger.info("Application configuration complete")
}

val httpClient = HttpClient(CIO) {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
    expectSuccess = false

    Charsets {
        register(Charsets.UTF_8)
    }
}
