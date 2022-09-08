package com.shadowconnect.plugins

import com.shadowconnect.routes.organizationRoutes
import com.shadowconnect.routes.professionalRouting
import com.shadowconnect.routes.studentRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

//todo add note about what this class does
fun Application.configureRouting() {

    routing {
        studentRouting()
        organizationRoutes()
        professionalRouting()
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
