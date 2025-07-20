package com.shadowconnect.plugins

import com.shadowconnect.auth.UserSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

fun Application.configureAuthentication() {
    install(Authentication) {
        session<UserSession>("session-auth") {
            validate { session ->
                // Session is valid if it exists and has required fields
                if (session.userId > 0 && session.email.isNotEmpty()) {
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf(
                        "error" to "Authentication required",
                        "message" to "Please log in to access this resource"
                    )
                )
            }
        }
    }
}