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
        
        // Serve the web frontend index page
        get("/") {
            call.respondFile(File("../web/build/dist/js/productionExecutable/index.html"))
        }
        
        // Serve JS and other static files 
        staticFiles("/static", File("../web/build/dist/js/productionExecutable"))
        
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
