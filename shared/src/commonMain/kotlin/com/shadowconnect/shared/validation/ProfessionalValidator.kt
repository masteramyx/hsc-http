@file:JsExport
@file:OptIn(ExperimentalJsExport::class)

package com.shadowconnect.shared.validation

import com.shadowconnect.shared.model.DayAvailability
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * Shared validation logic for professional registration and profile editing.
 * These rules apply across all platforms (Web, Android, iOS).
 */

    // Step 1: Basic Information Validators

    @JsExport
    fun validateFirstName(firstName: String): String? = when {
        firstName.trim().isBlank() -> "First name is required"
        else -> null
    }

    @JsExport
    fun validateLastName(lastName: String): String? = when {
        lastName.trim().isBlank() -> "Last name is required"
        else -> null
    }

    @JsExport
    fun validateEmail(email: String): String? = when {
        email.trim().isBlank() -> "Email is required"
        !Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+\$").matches(email) -> "Invalid email format"
        else -> null
    }

    @JsExport
    fun validatePhone(phone: String): String? = when {
        phone.trim().isBlank() -> "Phone number is required"
        else -> null
    }

    @JsExport
    fun validatePassword(password: String): String? = when {
        password.isBlank() -> "Password is required"
        password.length < 8 -> "Password must be at least 8 characters"
        else -> null
    }

    // Step 2: Professional Details Validators

    @JsExport
    fun validateProfessionalType(professionalType: String): String? = when {
        professionalType.isBlank() -> "Professional type is required"
        else -> null
    }

    @JsExport
    fun validateLicenseNumber(licenseNumber: String): String? = when {
        licenseNumber.trim().isBlank() -> "License number is required"
        else -> null
    }

    @JsExport
    fun validateLicenseState(licenseState: String): String? = when {
        licenseState.isBlank() -> "License state is required"
        else -> null
    }

    @JsExport
    fun validateMedicalSpecialty(medicalSpecialty: String): String? = when {
        medicalSpecialty.isBlank() -> "Medical specialty is required"
        else -> null
    }

    @JsExport
    fun validateYearsExperience(yearsExperience: String): String? = when {
        yearsExperience.isBlank() -> "Years of experience is required"
        else -> null
    }

    // Step 3: Practice Information Validators

    @JsExport
    fun validatePracticeType(practiceType: String): String? = when {
        practiceType.isBlank() -> "Practice type is required"
        else -> null
    }

    @JsExport
    fun validatePracticeName(practiceName: String): String? = when {
        practiceName.trim().isBlank() -> "Practice name is required"
        else -> null
    }

    @JsExport
    fun validatePracticeAddress(practiceAddress: String): String? = when {
        practiceAddress.trim().isBlank() -> "Address is required"
        else -> null
    }

    @JsExport
    fun validatePracticeCity(practiceCity: String): String? = when {
        practiceCity.trim().isBlank() -> "City is required"
        else -> null
    }

    @JsExport
    fun validatePracticeState(practiceState: String): String? = when {
        practiceState.isBlank() -> "State is required"
        else -> null
    }

    @JsExport
    fun validatePracticeZip(practiceZip: String): String? = when {
        practiceZip.trim().isBlank() -> "ZIP code is required"
        else -> null
    }

    @JsExport
    fun validateTitlePosition(titlePosition: String): String? = when {
        titlePosition.trim().isBlank() -> "Title/Position is required"
        else -> null
    }

    // Step 4: Availability Validators

    @JsExport
    fun validateAvailability(availability: List<DayAvailability>): String? = when {
        availability.isEmpty() -> "Please select at least one available day"
        else -> null
    }

    // Step 5: Profile Validators

    @JsExport
    fun validateBio(bio: String): String? = when {
        bio.trim().isBlank() -> "Bio is required"
        else -> null
    }

    // Generic validator for any required field
    @JsExport
    fun validateRequired(value: String, fieldName: String): String? = when {
        value.trim().isBlank() -> "$fieldName is required"
        else -> null
    }