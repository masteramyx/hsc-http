package com.shadowconnect

import com.shadowconnect.db.DatabaseRepositoryImpl
import io.ktor.server.application.*
import com.shadowconnect.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureRouting()
    configureSerialization()

    val d = DatabaseRepositoryImpl()
    d.testConnection()
}
