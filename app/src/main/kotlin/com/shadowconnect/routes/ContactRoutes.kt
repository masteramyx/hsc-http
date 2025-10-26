package com.shadowconnect.routes

import com.shadowconnect.db.ContactSubmissionRepositoryImpl
import com.shadowconnect.db.DatabaseFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class ContactSubmissionRequest(
    val name: String,
    val email: String,
    val userType: String,
    val message: String
)

fun Route.contactRouting() {
    val database = DatabaseFactory.getDatabase()
    val contactRepository = ContactSubmissionRepositoryImpl(database)

    post("/api/contact") {
        try {
            val request = call.receive<ContactSubmissionRequest>()

            // Validate user type
            if (request.userType !in listOf("professional", "student")) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Invalid user type. Must be 'professional' or 'student'")
                )
                return@post
            }

            // Validate email format (basic)
            if (!request.email.contains("@")) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Invalid email format")
                )
                return@post
            }

            // Insert contact submission
            val id = contactRepository.insertContactSubmission(
                name = request.name,
                email = request.email,
                userType = request.userType,
                message = request.message
            )

            call.respond(
                HttpStatusCode.Created,
                mapOf(
                    "success" to true,
                    "id" to id,
                    "message" to "Contact submission received successfully"
                )
            )
        } catch (e: Exception) {
            println("Error saving contact submission: ${e.message}")
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (e.message ?: "Unknown error occurred"))
            )
        }
    }

    // Get all contact submissions (for admin use)
    get("/api/contact/submissions") {
        try {
            val submissions = contactRepository.getAllContactSubmissions()
            call.respond(HttpStatusCode.OK, submissions)
        } catch (e: Exception) {
            println("Error fetching contact submissions: ${e.message}")
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (e.message ?: "Unknown error occurred"))
            )
        }
    }
}
