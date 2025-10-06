package com.shadowconnect.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

/**
 * Configures Cross-Origin Resource Sharing (CORS) for the application.
 *
 * In simple terms, this works like a security guard for your server. By default, a web browser
 * will block a frontend application from making requests to a backend on a different address
 * (e.g., a mobile app talking to your server, or a separate frontend development server).
 *
 * This configuration tells the browser which "origins" (addresses) are safe to accept
 * requests from. While not strictly needed when the frontend and backend are served from the
 * exact same address, it's essential for future-proofing, enabling mobile apps, or allowing
 * for more flexible development setups.
 */
fun Application.configureCORS() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowCredentials = true

        // Allow specific localhost origins (required when allowCredentials = true)
        allowHost("localhost:8081", listOf("http", "https"))
        allowHost("127.0.0.1:8081", listOf("http", "https"))
        allowHost("localhost:8080", listOf("http", "https"))
        allowHost("127.0.0.1:8080", listOf("http", "https"))

        // Allow Render deployment
        allowHost("hsc-http-backend.onrender.com", listOf("https"))
        allowHost("shadowconnects.com", listOf("https", "http"))

        // Note: anyHost() cannot be used with allowCredentials = true
    }
}