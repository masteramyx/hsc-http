package com.shadowconnect.plugins

import com.shadowconnect.routes.userRouting
import com.shadowconnect.routes.authRouting
import com.shadowconnect.routes.chatRouting
import com.shadowconnect.routes.contactRouting
import com.shadowconnect.routes.emailVerificationRouting
import com.shadowconnect.routes.fileUploadRouting
import com.shadowconnect.routes.professionalRouting
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

@Serializable
data class DebugFilesResponse(
    val reactWebExists: Boolean,
    val reactWebPath: String,
    val reactWebContents: List<String>,
    val buildDirExists: Boolean,
    val buildDirPath: String,
    val buildContents: List<String>
)

//todo add note about what this class does
fun Application.configureRouting() {

    routing {
        userRouting()
        authRouting()
        chatRouting()
        contactRouting()
        emailVerificationRouting()
        fileUploadRouting()
        professionalRouting()
        
        // This logic handles serving the correct frontend files for different environments.
        // Priority: React frontend > Compose Web frontend
        // When running in a Docker container, it serves the production build.
        // When running locally for development, it serves the development build.
        get("/") {
            // Check for React build first
            val reactDockerPath = File("/app/react-web/build/index.html")
            val reactLocalPath = File("../react-web/build/index.html")

            // Fallback to Compose Web
            val composeDockerPath = File("/app/web/build/processedResources/js/main/index.html")
            val composeLocalPath = File("../web/build/processedResources/js/main/index.html")

            val indexFile = when {
                reactDockerPath.exists() -> {
                    application.log.info("Serving React build from: ${reactDockerPath.absolutePath}")
                    reactDockerPath
                }
                reactLocalPath.exists() -> {
                    application.log.info("Serving React build from: ${reactLocalPath.absolutePath}")
                    reactLocalPath
                }
                composeDockerPath.exists() -> {
                    application.log.info("Serving Compose Web from: ${composeDockerPath.absolutePath}")
                    composeDockerPath
                }
                else -> {
                    application.log.error("No frontend build found! Checked: $reactDockerPath, $reactLocalPath, $composeDockerPath, $composeLocalPath")
                    call.respondText("Frontend build not found", status = HttpStatusCode.NotFound)
                    return@get
                }
            }
            call.respondFile(indexFile)
        }

        // Serve React static assets (Vite build output)
        val reactDockerStaticPath = File("/app/react-web/build/assets")
        val reactLocalStaticPath = File("../react-web/build/assets")
        if (reactDockerStaticPath.exists() || reactLocalStaticPath.exists()) {
            val reactStaticPath = if (reactDockerStaticPath.exists()) reactDockerStaticPath else reactLocalStaticPath
            staticFiles("/assets", reactStaticPath)
        }

        // Serve the Compose Web compiled JavaScript and other static assets.
        // It checks for the production path first (for Docker) and falls back to the
        // development path for local runs.
        val composeDockerStaticPath = File("/app/web/build/kotlin-webpack/js/productionExecutable")
        val composeLocalStaticPath = File("../web/build/kotlin-webpack/js/developmentExecutable")
        if (composeDockerStaticPath.exists() || composeLocalStaticPath.exists()) {
            val composeStaticPath = if (composeDockerStaticPath.exists()) composeDockerStaticPath else composeLocalStaticPath
            staticFiles("/static", composeStaticPath)
        }

        // Serve robots.txt for SEO
        get("/robots.txt") {
            val robotsDockerPath = File("/app/react-web/build/robots.txt")
            val robotsLocalPath = File("../react-web/build/robots.txt")

            val robotsFile = when {
                robotsDockerPath.exists() -> robotsDockerPath
                robotsLocalPath.exists() -> robotsLocalPath
                else -> {
                    call.respondText("User-agent: *\nAllow: /", ContentType.Text.Plain, HttpStatusCode.OK)
                    return@get
                }
            }
            call.respondFile(robotsFile)
        }

        // Serve sitemap.xml for SEO
        get("/sitemap.xml") {
            val sitemapDockerPath = File("/app/react-web/build/sitemap.xml")
            val sitemapLocalPath = File("../react-web/build/sitemap.xml")

            val sitemapFile = when {
                sitemapDockerPath.exists() -> sitemapDockerPath
                sitemapLocalPath.exists() -> sitemapLocalPath
                else -> {
                    call.respondText("Sitemap not found", status = HttpStatusCode.NotFound)
                    return@get
                }
            }
            call.respondText(
                sitemapFile.readText(),
                ContentType.Application.Xml,
                HttpStatusCode.OK
            )
        }

        get("/debug-files") {
            val reactWebDir = File("/app/react-web")
            val buildDir = File("/app/react-web/build")

            val response = DebugFilesResponse(
                reactWebExists = reactWebDir.exists(),
                reactWebPath = reactWebDir.absolutePath,
                reactWebContents = reactWebDir.listFiles()?.map { it.name } ?: emptyList(),
                buildDirExists = buildDir.exists(),
                buildDirPath = buildDir.absolutePath,
                buildContents = buildDir.listFiles()?.map { it.name } ?: emptyList()
            )

            call.respond(response)
        }

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

        // SPA Fallback: Catch-all route for client-side routing
        // This must be last to avoid catching API routes and static assets
        // Serves index.html for all non-API, non-static routes to enable React Router
        get("{...}") {
            val reactDockerPath = File("/app/react-web/build/index.html")
            val reactLocalPath = File("../react-web/build/index.html")
            val composeDockerPath = File("/app/web/build/processedResources/js/main/index.html")

            val indexFile = when {
                reactDockerPath.exists() -> reactDockerPath
                reactLocalPath.exists() -> reactLocalPath
                composeDockerPath.exists() -> composeDockerPath
                else -> {
                    call.respondText("Frontend build not found", status = HttpStatusCode.NotFound)
                    return@get
                }
            }
            call.respondFile(indexFile)
        }
    }
}
