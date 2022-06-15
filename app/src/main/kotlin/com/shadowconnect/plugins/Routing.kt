package com.shadowconnect.plugins

import com.shadowconnect.routes.userRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

//todo add note about what this class does
fun Application.configureRouting() {

    routing {
        userRouting()
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
