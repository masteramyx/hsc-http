package com.shadowconnect.db

import com.healthshadow.db.HealthShadowDatabase
import com.shadowconnect.core.model.ProfessionalType
import com.shadowconnect.core.model.MedicalSpecialty
import com.shadowconnect.core.model.PracticeType

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
    ): Boolean {
        return try {
            database.professionalQueries.createProfessional(
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
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
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
}
