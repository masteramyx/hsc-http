package com.shadowconnect.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

//todo add note about what this class does
fun Application.configureSerialization() {
    /**
     *   GET http://127.0.0.1:8080/customer
     *   Accept: application/json
     *
     *   ContentNegotiation plugin examines the Accept header of request to see if we can serve this specific
     *   type of content
     */
    install(ContentNegotiation) {
        json(
            kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            }
        )
    }

    routing {
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}
