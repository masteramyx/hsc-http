package com.shadowconnect.db

import com.shadowconnect.shared.model.ProfessionalType
import com.shadowconnect.shared.model.MedicalSpecialty
import com.shadowconnect.shared.model.PracticeType

/**
 * Result of atomic professional registration containing both user and professional IDs
 */
data class RegistrationResult(
    val userId: Long,
    val professionalId: Long
)

data class ProfessionalData(
    val id: Long,
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val professionalType: ProfessionalType,
    val licenseNumber: String?,
    val specialization: MedicalSpecialty?,
    val yearsExperience: Int?,
    val organization: String?,
    val practiceType: PracticeType?,
    val practiceCity: String?,
    val practiceState: String?,
    val practiceAddress: String?,
    val specialties: String?,
    val studentRequirements: String?,
    val availableDays: String?,
    val availableTimes: String?,
    val title: String?,
    val bio: String?,
    val photoUrl: String?,
    val verified: Boolean,
    val email: String,
    val userType: String,
    val isActive: Boolean?
)

interface ProfessionalRepository {
    fun getAllActiveProfessionals(): List<ProfessionalData>
    fun getProfessionalById(id: Long): ProfessionalData?
    fun getProfessionalByUserId(userId: Long): ProfessionalData?
    fun createProfessional(
        userId: Long,
        firstName: String,
        lastName: String,
        phone: String,
        professionalType: ProfessionalType,
        licenseNumber: String? = null,
        specialization: MedicalSpecialty? = null,
        yearsExperience: Int? = null,
        organization: String? = null,
        practiceType: PracticeType? = null,
        practiceCity: String? = null,
        practiceState: String? = null,
        practiceAddress: String? = null,
        title: String? = null,
        bio: String? = null,
        photoUrl: String? = null,
        specialties: String? = null,
        studentRequirements: String? = null,
        availableDays: String? = null,
        availableTimes: String? = null
    ): Long
    fun updateProfessionalProfile(
        userId: Long,
        firstName: String,
        lastName: String,
        phone: String,
        professionalType: ProfessionalType,
        photoUrl: String? = null,
        specialization: MedicalSpecialty? = null,
        practiceType: PracticeType? = null,
        practiceCity: String? = null,
        practiceState: String? = null,
        practiceAddress: String? = null,
        bio: String? = null,
        specialties: String? = null,
        studentRequirements: String? = null,
        availableDays: String? = null,
        availableTimes: String? = null
    ): Boolean

    /**
     * Atomically register a new professional user - creates both user account and professional profile
     * in a single transaction. Either both succeed or both fail (rollback).
     *
     * @return RegistrationResult containing both user ID and professional ID
     * @throws Exception if registration fails (will trigger transaction rollback)
     */
    suspend fun registerNewProfessional(
        email: String,
        passwordHash: String,
        firstName: String,
        lastName: String,
        phone: String,
        professionalType: ProfessionalType,
        licenseNumber: String,
        licenseState: String,
        specialization: MedicalSpecialty,
        yearsExperience: Int,
        practiceType: PracticeType,
        practiceName: String,
        practiceAddress: String,
        practiceCity: String,
        practiceState: String,
        practiceZip: String,
        titlePosition: String,
        availabilityJson: String,  // JSON serialized DayAvailability list
        bio: String,
        availabilityNotes: String? = null,
        photoUrl: String? = null
    ): RegistrationResult
}
