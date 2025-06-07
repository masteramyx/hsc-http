package com.shadowconnect

import io.ktor.server.application.*
import com.shadowconnect.plugins.*
import com.shadowconnect.db.DatabaseFactory

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    // Initialize database connection
    DatabaseFactory.init()
    
    configureRouting()
    configureSerialization()
}
