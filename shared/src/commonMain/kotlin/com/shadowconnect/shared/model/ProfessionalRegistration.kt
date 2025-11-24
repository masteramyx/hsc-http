@file:JsExport
@file:OptIn(ExperimentalJsExport::class)

package com.shadowconnect.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

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
) {
    companion object {
        /**
         * Deserialize a JSON string containing an array of DayAvailability objects
         * Example: "[{\"day\":\"MONDAY\",\"time_ranges\":[\"MORNING\",\"AFTERNOON\"]}]"
         * Returns a JavaScript array (not KtList) for direct use in JS/React
         * Also converts nested timeRanges lists to JS arrays
         */
        @JsName("fromJsonArray")
        fun fromJsonArray(jsonString: String): Array<DayAvailabilityJS> {
            val list = Json.decodeFromString(ListSerializer(serializer()), jsonString)
            // Convert to JS-friendly format with arrays instead of Lists
            return list.map { dayAvail ->
                DayAvailabilityJS(
                    day = dayAvail.day,
                    timeRanges = dayAvail.timeRanges.toTypedArray()
                )
            }.toTypedArray()
        }

        /**
         * Converts JS Array back to Kotlin List for backend consumption
         */
        @JsName("toKtList")
        fun toKtList(jsArray: Array<DayAvailabilityJS>): List<DayAvailability> {
            return jsArray.map { jsAvail ->
                DayAvailability(
                    day = jsAvail.day,
                    timeRanges = jsAvail.timeRanges.toList()
                )
            }
        }
    }
}

/**
 * JavaScript-friendly version of DayAvailability with Array instead of List for timeRanges
 */
data class DayAvailabilityJS(
    val day: DayOfWeek,
    val timeRanges: Array<TimeRange>
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


/**
 * Request model for updating an existing professional profile.
 * Excludes email/password which are updated via separate endpoints.
 */
@Serializable
data class UpdateProfessionalRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val professionalType: ProfessionalType,
    val licenseNumber: String?,
    val licenseState: String?,
    val specialization: MedicalSpecialty?,
    val yearsExperience: Int?,
    val practiceType: PracticeType?,
    val practiceName: String?,
    val practiceAddress: String?,
    val practiceCity: String?,
    val practiceState: String?,
    val practiceZip: String?,
    val titlePosition: String?,
    val bio: String?,
    val photoUrl: String?,
    val availability: List<DayAvailability>,
    val availabilityNotes: String?
) {
    fun toJsonString(): String {
        return Json.encodeToString(serializer(), this)
    }

    companion object {
        /**
         * Factory method for creating UpdateProfessionalRequest from JS-friendly primitives
         * Handles string→enum conversion and Array→List conversion at the boundary
         */
        @JsName("fromJsData")
        fun fromJsData(
            firstName: String,
            lastName: String,
            phone: String,
            professionalType: String,  // String, not enum
            licenseNumber: String?,
            licenseState: String?,
            specialization: String?,  // String, not enum
            yearsExperience: Int?,
            practiceType: String?,  // String, not enum
            practiceName: String?,
            practiceAddress: String?,
            practiceCity: String?,
            practiceState: String?,
            practiceZip: String?,
            titlePosition: String?,
            bio: String?,
            photoUrl: String?,
            availability: Array<DayAvailabilityJS>,  // JS-friendly array
            availabilityNotes: String?
        ): UpdateProfessionalRequest {
            return UpdateProfessionalRequest(
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                professionalType = ProfessionalType.valueOf(professionalType),
                licenseNumber = licenseNumber,
                licenseState = licenseState,
                specialization = specialization?.let { MedicalSpecialty.valueOf(it) },
                yearsExperience = yearsExperience,
                practiceType = practiceType?.let { PracticeType.valueOf(it) },
                practiceName = practiceName,
                practiceAddress = practiceAddress,
                practiceCity = practiceCity,
                practiceState = practiceState,
                practiceZip = practiceZip,
                titlePosition = titlePosition,
                bio = bio,
                photoUrl = photoUrl,
                availability = DayAvailability.toKtList(availability),
                availabilityNotes = availabilityNotes
            )
        }
    }
}
