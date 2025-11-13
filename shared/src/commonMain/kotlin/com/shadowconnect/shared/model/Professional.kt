@file:JsExport
@file:OptIn(ExperimentalJsExport::class)

package com.shadowconnect.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * Professional user profile - represents a healthcare professional in the system.
 * This is used as the API response DTO across all platforms (web, mobile, backend).
 */
@Serializable
data class Professional(
    @SerialName("id")
    val id: Long,

    @SerialName("user_id")
    val userId: Long,

    @SerialName("first_name")
    val firstName: String,

    @SerialName("last_name")
    val lastName: String,

    @SerialName("email")
    val email: String,

    @SerialName("phone")
    val phone: String,

    @SerialName("professional_type")
    val professionalType: ProfessionalType,

    @SerialName("license_number")
    val licenseNumber: String? = null,

    @SerialName("specialization")
    val specialization: MedicalSpecialty? = null,

    @SerialName("years_experience")
    val yearsExperience: Int? = null,

    @SerialName("organization")
    val organization: String? = null,

    @SerialName("practice_type")
    val practiceType: PracticeType? = null,

    @SerialName("practice_city")
    val practiceCity: String? = null,

    @SerialName("practice_state")
    val practiceState: String? = null,

    @SerialName("practice_address")
    val practiceAddress: String? = null,

    @SerialName("specialties")
    val specialties: String? = null, // JSON array of additional specialties

    @SerialName("student_requirements")
    val studentRequirements: String? = null,

    @SerialName("available_days")
    val availableDays: String? = null, // JSON array

    @SerialName("available_times")
    val availableTimes: String? = null,

    @SerialName("title")
    val title: String? = null,

    @SerialName("bio")
    val bio: String? = null,

    @SerialName("photo_url")
    val photoUrl: String? = null,

    @SerialName("verified")
    val verified: Boolean = false,

    @SerialName("user_type")
    val userType: String,

    @SerialName("is_active")
    val isActive: Boolean
)
