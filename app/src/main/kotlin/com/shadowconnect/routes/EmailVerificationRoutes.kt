package com.shadowconnect.routes

import com.shadowconnect.auth.UserSession
import com.shadowconnect.db.DatabaseFactory
import com.shadowconnect.db.EmailVerificationTokenRepositoryImpl
import com.shadowconnect.db.UserRepositoryImpl
import com.shadowconnect.service.EmailClient
import com.shadowconnect.service.EmailVerificationService
import com.shadowconnect.service.VerificationResult
import com.shadowconnect.utils.logger
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable

@Serializable
data class VerifyEmailResponse(
    val success: Boolean,
    val message: String
)

fun Route.emailVerificationRouting() {
    val database = DatabaseFactory.getDatabase()
    val userRepository = UserRepositoryImpl(database)
    val tokenRepository = EmailVerificationTokenRepositoryImpl(database)
    val emailClient = EmailClient()
    val verificationService = EmailVerificationService(
        tokenRepository,
        userRepository,
        emailClient
    )

    route("/api/v1/email") {
        // Public endpoint - verify email with token
        get("/verify") {
            val token = call.request.queryParameters["token"]

            if (token == null) {
                return@get call.respond(
                    HttpStatusCode.BadRequest,
                    VerifyEmailResponse(false, "Token is required")
                )
            }

            logger.debug("Email verification requested with token: ${token.take(10)}...")

            when (verificationService.verifyEmail(token)) {
                VerificationResult.SUCCESS -> {
                    call.respond(
                        HttpStatusCode.OK,
                        VerifyEmailResponse(true, "Email verified successfully!")
                    )
                }
                VerificationResult.INVALID_TOKEN -> {
                    call.respond(
                        HttpStatusCode.NotFound,
                        VerifyEmailResponse(false, "Invalid verification token")
                    )
                }
                VerificationResult.ALREADY_USED -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        VerifyEmailResponse(false, "This verification link has already been used")
                    )
                }
                VerificationResult.EXPIRED -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        VerifyEmailResponse(false, "This verification link has expired. Please request a new one.")
                    )
                }
            }
        }

        // Protected endpoint - resend verification email
        authenticate("session-auth") {
            post("/resend-verification") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        VerifyEmailResponse(false, "Not authenticated")
                    )
                }

                logger.debug("Resend verification requested for user ${session.userId}")

                val success = verificationService.resendVerificationEmail(session.userId)

                if (success) {
                    call.respond(
                        HttpStatusCode.OK,
                        VerifyEmailResponse(true, "Verification email sent!")
                    )
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        VerifyEmailResponse(false, "Failed to send verification email")
                    )
                }
            }
        }
    }
}