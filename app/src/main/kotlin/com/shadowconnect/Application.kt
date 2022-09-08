package com.shadowconnect

import com.shadowconnect.plugins.configureDatabase
import com.shadowconnect.plugins.configureRouting
import com.shadowconnect.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureRouting()
    configureSerialization()
    configureDatabase()
}
