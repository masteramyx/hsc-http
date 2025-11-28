package com.shadowconnect.routes

import com.shadowconnect.auth.AuthService
import com.shadowconnect.auth.UserSession
import com.shadowconnect.db.DatabaseFactory
import com.shadowconnect.db.UserRepositoryImpl
import com.shadowconnect.shared.model.LoginRequest
import com.shadowconnect.shared.model.LoginResponse
import com.shadowconnect.shared.model.LogoutResponse
import com.shadowconnect.shared.model.UserInfo
import com.shadowconnect.shared.model.UserType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.authRouting() {
    val database = DatabaseFactory.getDatabase()
    val userRepository = UserRepositoryImpl(database)
    val authService = AuthService(userRepository)

    // Public auth endpoints
    post("/login") {
        try {
            val loginRequest = call.receive<LoginRequest>()
            val user = authService.authenticateUser(loginRequest.email, loginRequest.password)
            
            if (user != null) {
                // Create session
                val userType = UserType.fromString(user.user_type)
                val session = UserSession(
                    userId = user.id,
                    email = user.email,
                    userType = userType
                )
                call.sessions.set(session)

                call.respond(
                    HttpStatusCode.OK,
                    LoginResponse(
                        success = true,
                        message = "Login successful",
                        user = UserInfo(
                            id = user.id,
                            email = user.email,
                            userType = userType,
                            emailVerified = user.email_verified ?: false
                        )
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    LoginResponse(success = false, message = "Invalid email or password")
                )
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.BadRequest,
                LoginResponse(success = false, message = "Invalid request format")
            )
        }
    }

    // Protected endpoints requiring authentication
    authenticate("session-auth") {
        post("/logout") {
            call.sessions.clear<UserSession>()
            call.respond(
                HttpStatusCode.OK,
                LogoutResponse(success = true, message = "Logged out successfully")
            )
        }

        get("/me") {
            val session = call.sessions.get<UserSession>()
            if (session != null) {
                val user = userRepository.findById(session.userId)
                call.respond(
                    HttpStatusCode.OK,
                    LoginResponse(
                        success = true,
                        message = "Authenticated",
                        user = UserInfo(
                            id = session.userId,
                            email = session.email,
                            userType = session.userType,
                            emailVerified = user?.email_verified ?: false
                        )
                    )
                )
            } else {
                // This shouldn't happen due to authenticate block, but handle gracefully
                call.respond(
                    HttpStatusCode.Unauthorized,
                    LoginResponse(success = false, message = "Session invalid")
                )
            }
        }

        get("/api/protected") {
            val session = call.sessions.get<UserSession>()
            if (session != null) {
                call.respond(
                    HttpStatusCode.OK,
                    mapOf(
                        "message" to "Welcome to the protected area!",
                        "user" to session.email,
                        "timestamp" to java.time.Instant.now().toString()
                    )
                )
            } else {
                // Fallback - shouldn't happen due to authenticate block
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "Session invalid")
                )
            }
        }
        
        get("/api/profile") {
            val session = call.sessions.get<UserSession>()
            if (session != null) {
                call.respond(
                    HttpStatusCode.OK,
                    mapOf(
                        "id" to session.userId,
                        "email" to session.email,
                        "userType" to session.userType
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "Session invalid")
                )
            }
        }
    }
}