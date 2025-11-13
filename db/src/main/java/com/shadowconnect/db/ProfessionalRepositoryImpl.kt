package com.shadowconnect.db

import com.healthshadow.db.HealthShadowDatabase
import com.shadowconnect.shared.model.ProfessionalType
import com.shadowconnect.shared.model.MedicalSpecialty
import com.shadowconnect.shared.model.PracticeType

class ProfessionalRepositoryImpl(
    private val database: HealthShadowDatabase
) : ProfessionalRepository {

    override fun getAllActiveProfessionals(): List<ProfessionalData> {
        return database.professionalQueries.getAllActiveProfessionals().executeAsList().map { row ->
            ProfessionalData(
                id = row.id,
                userId = row.user_id,
                firstName = row.first_name,
                lastName = row.last_name,
                phone = row.phone,
                professionalType = ProfessionalType.valueOf(row.professional_type),
                licenseNumber = row.license_number,
                specialization = row.specialization?.let { MedicalSpecialty.valueOf(it) },
                yearsExperience = row.years_experience,
                organization = row.organization,
                practiceType = row.practice_type?.let { PracticeType.valueOf(it) },
                practiceCity = row.practice_city,
                practiceState = row.practice_state,
                practiceAddress = row.practice_address,
                specialties = row.specialties,
                studentRequirements = row.student_requirements,
                availableDays = row.available_days,
                availableTimes = row.available_times,
                title = row.title,
                bio = row.bio,
                photoUrl = row.photo_url,
                verified = row.verified ?: false,
                email = row.email,
                userType = row.user_type,
                isActive = row.is_active
            )
        }
    }

    override fun getProfessionalById(id: Long): ProfessionalData? {
        return database.professionalQueries.getProfessionalById(id).executeAsOneOrNull()?.let { row ->
            ProfessionalData(
                id = row.id,
                userId = row.user_id,
                firstName = row.first_name,
                lastName = row.last_name,
                phone = row.phone,
                professionalType = ProfessionalType.valueOf(row.professional_type),
                licenseNumber = row.license_number,
                specialization = row.specialization?.let { MedicalSpecialty.valueOf(it) },
                yearsExperience = row.years_experience,
                organization = row.organization,
                practiceType = row.practice_type?.let { PracticeType.valueOf(it) },
                practiceCity = row.practice_city,
                practiceState = row.practice_state,
                practiceAddress = row.practice_address,
                specialties = row.specialties,
                studentRequirements = row.student_requirements,
                availableDays = row.available_days,
                availableTimes = row.available_times,
                title = row.title,
                bio = row.bio,
                photoUrl = row.photo_url,
                verified = row.verified ?: false,
                email = row.email,
                userType = row.user_type,
                isActive = row.is_active
            )
        }
    }

    override fun getProfessionalByUserId(userId: Long): ProfessionalData? {
        return database.professionalQueries.getProfessionalByUserId(userId).executeAsOneOrNull()?.let { row ->
            ProfessionalData(
                id = row.id,
                userId = row.user_id,
                firstName = row.first_name,
                lastName = row.last_name,
                phone = row.phone,
                professionalType = ProfessionalType.valueOf(row.professional_type),
                licenseNumber = row.license_number,
                specialization = row.specialization?.let { MedicalSpecialty.valueOf(it) },
                yearsExperience = row.years_experience,
                organization = row.organization,
                practiceType = row.practice_type?.let { PracticeType.valueOf(it) },
                practiceCity = row.practice_city,
                practiceState = row.practice_state,
                practiceAddress = row.practice_address,
                specialties = row.specialties,
                studentRequirements = row.student_requirements,
                availableDays = row.available_days,
                availableTimes = row.available_times,
                title = row.title,
                bio = row.bio,
                photoUrl = row.photo_url,
                verified = row.verified ?: false,
                email = row.email,
                userType = row.user_type,
                isActive = row.is_active
            )
        }
    }

    override fun createProfessional(
        userId: Long,
        firstName: String,
        lastName: String,
        phone: String,
        professionalType: ProfessionalType,
        licenseNumber: String?,
        specialization: MedicalSpecialty?,
        yearsExperience: Int?,
        organization: String?,
        practiceType: PracticeType?,
        practiceCity: String?,
        practiceState: String?,
        practiceAddress: String?,
        title: String?,
        bio: String?,
        photoUrl: String?,
        specialties: String?,
        studentRequirements: String?,
        availableDays: String?,
        availableTimes: String?
    ): Long {
        return database.professionalQueries.createProfessional(
            user_id = userId,
            first_name = firstName,
            last_name = lastName,
            phone = phone,
            professional_type = professionalType.name,
            license_number = licenseNumber,
            specialization = specialization?.name,
            years_experience = yearsExperience,
            organization = organization,
            practice_type = practiceType?.name,
            practice_city = practiceCity,
            practice_state = practiceState,
            practice_address = practiceAddress,
            title = title,
            bio = bio,
            photo_url = photoUrl,
            specialties = specialties,
            student_requirements = studentRequirements,
            available_days = availableDays,
            available_times = availableTimes
        ).executeAsOne()
    }

    override fun updateProfessionalProfile(
        userId: Long,
        firstName: String,
        lastName: String,
        phone: String,
        professionalType: ProfessionalType,
        photoUrl: String?,
        specialization: MedicalSpecialty?,
        practiceType: PracticeType?,
        practiceCity: String?,
        practiceState: String?,
        practiceAddress: String?,
        bio: String?,
        specialties: String?,
        studentRequirements: String?,
        availableDays: String?,
        availableTimes: String?
    ): Boolean {
        return try {
            database.professionalQueries.updateProfessionalProfile(
                first_name = firstName,
                last_name = lastName,
                phone = phone,
                professional_type = professionalType.name,
                photo_url = photoUrl,
                specialization = specialization?.name,
                practice_type = practiceType?.name,
                practice_city = practiceCity,
                practice_state = practiceState,
                practice_address = practiceAddress,
                bio = bio,
                specialties = specialties,
                student_requirements = studentRequirements,
                available_days = availableDays,
                available_times = availableTimes,
                user_id = userId
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Atomically register a new professional - creates user + professional profile in single transaction.
     * If any step fails, entire transaction rolls back automatically.
     */
    override suspend fun registerNewProfessional(
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
        availabilityJson: String,
        bio: String,
        availabilityNotes: String?,
        photoUrl: String?
    ): RegistrationResult {
        return database.transactionWithResult {
            // Step 1: Create user account
            val userId = database.userQueries.createUser(
                email = email,
                password_hash = passwordHash,
                user_type = "professional"
            ).executeAsOne()

            // Step 2: Create professional profile linked to user and get returned ID
            val professionalId = database.professionalQueries.createProfessional(
                user_id = userId,
                first_name = firstName,
                last_name = lastName,
                phone = phone,
                professional_type = professionalType.name,
                license_number = licenseNumber,
                specialization = specialization.name,
                years_experience = yearsExperience,
                organization = practiceName,  // Using practiceName as organization
                practice_type = practiceType.name,
                practice_city = practiceCity,
                practice_state = practiceState,
                practice_address = practiceAddress,
                title = titlePosition,
                bio = bio,
                photo_url = photoUrl,
                specialties = null,  // Can be added later
                student_requirements = availabilityNotes,
                available_days = availabilityJson,
                available_times = null  // Using availabilityJson for structured availability
            ).executeAsOne()

            // Return both IDs
            // If we reach here, transaction commits automatically
            RegistrationResult(userId = userId, professionalId = professionalId)
        }
    }
}
