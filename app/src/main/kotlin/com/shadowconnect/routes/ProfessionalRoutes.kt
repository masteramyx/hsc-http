package com.shadowconnect.routes

import com.shadowconnect.auth.AuthService
import com.shadowconnect.auth.UserSession
import com.shadowconnect.db.DatabaseFactory
import com.shadowconnect.db.ProfessionalRepositoryImpl
import com.shadowconnect.db.UserRepositoryImpl
import com.shadowconnect.shared.model.Professional
import com.shadowconnect.shared.model.ProfessionalRegistrationRequest
import com.shadowconnect.shared.model.ProfessionalRegistrationResponse
import com.shadowconnect.utils.logger
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.professionalRouting() {
    val database = DatabaseFactory.getDatabase()
    val professionalRepository = ProfessionalRepositoryImpl(database)
    val userRepository = UserRepositoryImpl(database)
    val authService = AuthService(userRepository)

    route("/api/v1") {
        // Public browsing endpoints
        route("/professionals") {
            // Get all active professionals
            get {
                logger.debug("GET /api/v1/professionals - Fetching all active professionals")
                try {
                    val professionalsData = professionalRepository.getAllActiveProfessionals()
                    val professionals = professionalsData.map { data ->
                        Professional(
                            id = data.id,
                            userId = data.userId,
                            firstName = data.firstName,
                            lastName = data.lastName,
                            email = data.email,
                            phone = data.phone,
                            professionalType = data.professionalType,
                            licenseNumber = data.licenseNumber,
                            specialization = data.specialization,
                            yearsExperience = data.yearsExperience,
                            organization = data.organization,
                            practiceType = data.practiceType,
                            practiceCity = data.practiceCity,
                            practiceState = data.practiceState,
                            practiceAddress = data.practiceAddress,
                            specialties = data.specialties,
                            studentRequirements = data.studentRequirements,
                            availableDays = data.availableDays,
                            availableTimes = data.availableTimes,
                            title = data.title,
                            bio = data.bio,
                            photoUrl = data.photoUrl,
                            verified = data.verified,
                            userType = data.userType,
                            isActive = data.isActive ?: false
                        )
                    }

                    if (professionals.isNotEmpty()) {
                        logger.debug("GET /api/v1/professionals - Returning ${professionals.size} professionals")
                        call.respond(professionals)
                    } else {
                        logger.debug("GET /api/v1/professionals - No professionals found")
                        call.respondText("No professionals found", status = HttpStatusCode.OK)
                    }
                } catch (e: Exception) {
                    logger.error("GET /api/v1/professionals - Error fetching professionals: ${e.message}", e)
                    call.respondText(
                        "Error fetching professionals: ${e.message}",
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }

            // Get professional by ID
            get("{id?}") {
                val idStr = call.parameters["id"] ?: return@get call.respondText(
                    "Missing id",
                    status = HttpStatusCode.BadRequest
                )

                val id = idStr.toLongOrNull() ?: return@get call.respondText(
                    "Invalid id format",
                    status = HttpStatusCode.BadRequest
                )

                logger.debug("GET /api/v1/professionals/$id - Fetching professional by ID")
                try {
                    val data = professionalRepository.getProfessionalById(id) ?: return@get call.respondText(
                        "No professional with id: $id",
                        status = HttpStatusCode.NotFound
                    )

                    val professional = Professional(
                        id = data.id,
                        userId = data.userId,
                        firstName = data.firstName,
                        lastName = data.lastName,
                        email = data.email,
                        phone = data.phone,
                        professionalType = data.professionalType,
                        licenseNumber = data.licenseNumber,
                        specialization = data.specialization,
                        yearsExperience = data.yearsExperience,
                        organization = data.organization,
                        practiceType = data.practiceType,
                        practiceCity = data.practiceCity,
                        practiceState = data.practiceState,
                        practiceAddress = data.practiceAddress,
                        specialties = data.specialties,
                        studentRequirements = data.studentRequirements,
                        availableDays = data.availableDays,
                        availableTimes = data.availableTimes,
                        title = data.title,
                        bio = data.bio,
                        photoUrl = data.photoUrl,
                        verified = data.verified,
                        userType = data.userType,
                        isActive = data.isActive ?: false
                    )

                    logger.debug("GET /api/v1/professionals/$id - Professional found and returned")
                    call.respond(professional)
                } catch (e: Exception) {
                    logger.error("GET /api/v1/professionals/$id - Error fetching professional: ${e.message}", e)
                    call.respondText(
                        "Error fetching professional: ${e.message}",
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }
        }

        // Professional-specific endpoints
        route("/professional") {
            // Public registration endpoint - atomic (user + professional) profile creation
            post("/register") {
                logger.debug("POST /api/v1/professional/register - Starting atomic professional registration")
                try {
                    val request = call.receive<ProfessionalRegistrationRequest>()

                    // Validate email doesn't already exist
                    if (userRepository.emailExists(request.email)) {
                        logger.warn("POST /api/v1/professional/register - Email already exists: ${request.email}")
                        return@post call.respond(
                            HttpStatusCode.Conflict,
                            ProfessionalRegistrationResponse(
                                success = false,
                                message = "Email address is already registered",
                                errorDetails = "A user with this email already exists in the system"
                            )
                        )
                    }

                    // Hash password
                    val passwordHash = authService.hashPassword(request.password)

                    // Serialize availability to JSON
                    val availabilityJson = Json.encodeToString(request.availability)

                    // Atomically create user + professional profile in transaction
                    val result = professionalRepository.registerNewProfessional(
                        email = request.email,
                        passwordHash = passwordHash,
                        firstName = request.firstName,
                        lastName = request.lastName,
                        phone = request.phone,
                        professionalType = request.professionalType,
                        licenseNumber = request.licenseNumber,
                        licenseState = request.licenseState,
                        specialization = request.specialization,
                        yearsExperience = request.yearsExperience,
                        practiceType = request.practiceType,
                        practiceName = request.practiceName,
                        practiceAddress = request.practiceAddress,
                        practiceCity = request.practiceCity,
                        practiceState = request.practiceState,
                        practiceZip = request.practiceZip,
                        titlePosition = request.titlePosition,
                        availabilityJson = availabilityJson,
                        bio = request.bio,
                        availabilityNotes = request.availabilityNotes,
                        photoUrl = request.photoUrl
                    )

                    logger.info("POST /api/v1/professional/register - Registration successful for userId=${result.userId}, professionalId=${result.professionalId}")
                    logger.info("POST /api/v1/professional/register - Auto-creating session with userId=${result.userId}")

                    val session = UserSession(
                        userId = result.userId,
                        email = request.email,
                        userType = "professional",
                    )
                    call.sessions.set(session)

                    call.respond(
                        HttpStatusCode.Created,
                        ProfessionalRegistrationResponse(
                            success = true,
                            message = "Professional registration successful",
                            userId = result.userId,
                            professionalId = result.professionalId
                        )
                    )
                } catch (e: Exception) {
                    // Transaction automatically rolled back on exception
                    logger.error("POST /api/v1/professional/register - Registration failed: ${e.message}", e)
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ProfessionalRegistrationResponse(
                            success = false,
                            message = "Registration failed",
                            errorDetails = e.message ?: "Unknown error occurred during registration"
                        )
                    )
                }
            }

            // Protected routes requiring authentication
            authenticate("session-auth") {
                // Get current user's professional profile
                get("/profile") {
                    val session = call.sessions.get<UserSession>()
                    if (session == null) {
                        logger.warn("GET /api/v1/professional/profile - No valid session")
                        return@get call.respond(
                            HttpStatusCode.Unauthorized,
                            mapOf("error" to "Not authenticated")
                        )
                    }

                    logger.debug("GET /api/v1/professional/profile - Fetching profile for user ${session.userId}")
                    try {
                        val professionalData = professionalRepository.getProfessionalByUserId(session.userId)

                        if (professionalData != null) {
                            val professional = Professional(
                                id = professionalData.id,
                                userId = professionalData.userId,
                                firstName = professionalData.firstName,
                                lastName = professionalData.lastName,
                                email = professionalData.email,
                                phone = professionalData.phone,
                                professionalType = professionalData.professionalType,
                                licenseNumber = professionalData.licenseNumber,
                                specialization = professionalData.specialization,
                                yearsExperience = professionalData.yearsExperience,
                                organization = professionalData.organization,
                                practiceType = professionalData.practiceType,
                                practiceCity = professionalData.practiceCity,
                                practiceState = professionalData.practiceState,
                                practiceAddress = professionalData.practiceAddress,
                                specialties = professionalData.specialties,
                                studentRequirements = professionalData.studentRequirements,
                                availableDays = professionalData.availableDays,
                                availableTimes = professionalData.availableTimes,
                                title = professionalData.title,
                                bio = professionalData.bio,
                                photoUrl = professionalData.photoUrl,
                                verified = professionalData.verified,
                                userType = professionalData.userType,
                                isActive = professionalData.isActive ?: false
                            )
                            logger.debug("GET /api/v1/professional/profile - Profile found and returned")
                            call.respond(professional)
                        } else {
                            logger.debug("GET /api/v1/professional/profile - No profile found for user")
                            call.respond(
                                HttpStatusCode.NotFound,
                                mapOf("error" to "Professional profile not found")
                            )
                        }
                    } catch (e: Exception) {
                        logger.error("GET /api/v1/professional/profile - Error fetching profile: ${e.message}", e)
                        call.respondText(
                            "Error fetching profile: ${e.message}",
                            status = HttpStatusCode.InternalServerError
                        )
                    }
                }

                // Update current user's professional profile
//                put("/profile") {
//                    val session = call.sessions.get<UserSession>()
//                    if (session == null) {
//                        logger.warn("PUT /api/v1/professional/profile - No valid session")
//                        return@put call.respond(
//                            HttpStatusCode.Unauthorized,
//                            mapOf("error" to "Not authenticated")
//                        )
//                    }
//
//                    logger.debug("PUT /api/v1/professional/profile - Updating profile for user ${session.userId}")
//                    try {
//                        val request: UpdateProfessionalRequest = call.receive()
//
//                        val success = professionalRepository.updateProfessionalProfile(
//                            userId = session.userId,
//                            firstName = request.firstName,
//                            lastName = request.lastName,
//                            phone = request.phone,
//                            professionalType = request.professionalType,
//                            photoUrl = request.photoUrl,
//                            specialization = request.specialization,
//                            practiceType = request.practiceType,
//                            practiceCity = request.practiceCity,
//                            practiceState = request.practiceState,
//                            practiceAddress = request.practiceAddress,
//                            bio = request.bio,
//                            specialties = request.specialties,
//                            studentRequirements = request.studentRequirements,
//                            availableDays = request.availableDays,
//                            availableTimes = request.availableTimes
//                        )
//
//                        if (success) {
//                            logger.info("PUT /api/v1/professional/profile - Profile updated successfully")
//                            call.respond(
//                                HttpStatusCode.OK,
//                                mapOf("message" to "Professional profile updated successfully")
//                            )
//                        } else {
//                            logger.warn("PUT /api/v1/professional/profile - Failed to update profile")
//                            call.respondText(
//                                "Failed to update professional profile",
//                                status = HttpStatusCode.InternalServerError
//                            )
//                        }
//                    } catch (e: Exception) {
//                        logger.error("PUT /api/v1/professional/profile - Error updating profile: ${e.message}", e)
//                        call.respondText(
//                            "Error updating profile: ${e.message}",
//                            status = HttpStatusCode.BadRequest
//                        )
//                    }
//                }
            }
        }
    }
}