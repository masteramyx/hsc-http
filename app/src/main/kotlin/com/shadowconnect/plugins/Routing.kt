package com.shadowconnect.plugins

import com.shadowconnect.routes.userRouting
import com.shadowconnect.routes.authRouting
import com.shadowconnect.db.DatabaseFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import java.io.File
import kotlinx.serialization.Serializable

@Serializable
data class HealthStatus(
    val status: String,
    val version: String,
    val database: String,
    val timestamp: String
)

//todo add note about what this class does
fun Application.configureRouting() {

    routing {
        userRouting()
        authRouting()
        
        // This logic handles serving the correct frontend files for different environments.
        // When running in a Docker container, it serves the production build.
        // When running locally for development, it serves the development build.
        get("/") {
            val dockerPath = File("/app/web/build/processedResources/js/main/index.html")
            val localPath = File("../web/build/processedResources/js/main/index.html")
            val indexFile = if (dockerPath.exists()) dockerPath else localPath
            call.respondFile(indexFile)
        }
        
        // Serve the compiled JavaScript and other static assets.
        // It checks for the production path first (for Docker) and falls back to the
        // development path for local runs.
        val dockerStaticPath = File("/app/web/build/kotlin-webpack/js/productionExecutable")
        val localStaticPath = File("../web/build/kotlin-webpack/js/developmentExecutable")
        val staticPath = if (dockerStaticPath.exists()) dockerStaticPath else localStaticPath
        staticFiles("/static", staticPath)
        
        get("/health") {
            try {
                // Test database connection by querying students
                val database = DatabaseFactory.getDatabase()
                val studentQueries = database.studentQueries
                studentQueries.getAllActiveStudents().executeAsList()
                
                call.respond(HttpStatusCode.OK, HealthStatus(
                    status = "healthy",
                    version = "1.0.0",
                    database = "connected",
                    timestamp = java.time.Instant.now().toString()
                ))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ServiceUnavailable, HealthStatus(
                    status = "unhealthy",
                    version = "1.0.0", 
                    database = "disconnected",
                    timestamp = java.time.Instant.now().toString()
                ))
            }
        }
    }
}
