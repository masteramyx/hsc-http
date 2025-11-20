@file:JsExport
@file:OptIn(ExperimentalJsExport::class)

package com.shadowconnect.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * Complete professional registration request combining user account creation
 * and professional profile setup in a single atomic operation.
 *
 * This is used by all client platforms (React, future Android/iOS) to register
 * new professional users.
 */
@Serializable
data class ProfessionalRegistrationRequest(
    // Step 1: Basic Information (User Account)
    @SerialName("email")
    val email: String,

    @SerialName("password")
    val password: String,

    @SerialName("first_name")
    val firstName: String,

    @SerialName("last_name")
    val lastName: String,

    @SerialName("phone")
    val phone: String,

    @SerialName("photo_url")
    val photoUrl: String? = null,

    // Step 2: Professional Details
    @SerialName("professional_type")
    val professionalType: ProfessionalType,

    @SerialName("license_number")
    val licenseNumber: String,

    @SerialName("license_state")
    val licenseState: String,

    @SerialName("specialization")
    val specialization: MedicalSpecialty,

    @SerialName("years_experience")
    val yearsExperience: Int,

    // Step 3: Practice Information
    @SerialName("practice_type")
    val practiceType: PracticeType,

    @SerialName("practice_name")
    val practiceName: String,

    @SerialName("practice_address")
    val practiceAddress: String,

    @SerialName("practice_city")
    val practiceCity: String,

    @SerialName("practice_state")
    val practiceState: String,

    @SerialName("practice_zip")
    val practiceZip: String,

    @SerialName("title_position")
    val titlePosition: String,

    // Step 4: Availability
    @SerialName("availability")
    val availability: List<DayAvailability>,

    // Step 5: Profile
    @SerialName("bio")
    val bio: String,

    @SerialName("availability_notes")
    val availabilityNotes: String? = null
)

/**
 * Day availability with time ranges.
 * Used within ProfessionalRegistrationRequest to specify when a professional is available.
 */
@Serializable
data class DayAvailability(
    @SerialName("day")
    val day: DayOfWeek,

    @SerialName("time_ranges")
    val timeRanges: List<TimeRange>
)

/**
 * Response sent back after attempting professional registration.
 * Used by all client platforms to handle registration results.
 */
@Serializable
data class ProfessionalRegistrationResponse(
    @SerialName("success")
    val success: Boolean,

    @SerialName("message")
    val message: String,

    @SerialName("user_id")
    val userId: Long? = null,

    @SerialName("professional_id")
    val professionalId: Long? = null,

    @SerialName("error_details")
    val errorDetails: String? = null
)
