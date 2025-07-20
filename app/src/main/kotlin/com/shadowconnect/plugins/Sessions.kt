package com.shadowconnect.plugins

import com.shadowconnect.auth.UserSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*

fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserSession>("hsc_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 3600 * 24 // 24 hours
            cookie.httpOnly = true
            cookie.secure = false // Set to true in production with HTTPS
        }
    }
}