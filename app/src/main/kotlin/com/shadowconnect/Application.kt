package com.shadowconnect

import io.ktor.server.application.*
import com.shadowconnect.plugins.*
import com.shadowconnect.db.DatabaseFactory
import com.shadowconnect.utils.logger
import io.ktor.server.auth.*
import io.ktor.server.sessions.*
import com.shadowconnect.auth.UserSession

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    logger.info("Starting HSC-HTTP application")
    
    // Initialize database connection
    DatabaseFactory.init()
    logger.info("Database initialized successfully")
    
    configureSessions()
    configureAuthentication()
    configureRouting()
    configureSerialization()
    
    logger.info("Application configuration complete")
}
