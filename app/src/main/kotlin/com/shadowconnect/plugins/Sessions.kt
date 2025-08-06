package com.shadowconnect.plugins

import com.shadowconnect.auth.UserSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*

fun Application.configureSessions() {
    install(Sessions) {
        cookie<UserSession>("hsc_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 3600 * 24 // 24 hours

            // httpOnly = true: Protects the key at its destination.
            // This flag tells the browser: "Do not let any JavaScript running on this page touch this cookie."
            // It's a critical defense against Cross-Site Scripting (XSS) attacks. If an attacker injects
            // malicious script onto the page, this prevents that script from stealing the session cookie.
            cookie.httpOnly = true

            // secure = true: Protects the key in transit.
            // This flag tells the browser: "Only send this cookie back to the server over an encrypted HTTPS connection."
            // This is essential in production to prevent Man-in-the-Middle (MITM) attacks, where an attacker
            // on the same network (e.g., public Wi-Fi) could sniff the unencrypted traffic and steal the cookie.
            // It is set to false for local development, which uses insecure HTTP.
            cookie.secure = false
        }
    }
}