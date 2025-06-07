package com.shadowconnect.db

import com.healthshadow.db.HealthShadowDatabase

class StudentRepositoryImpl(
    private val database: HealthShadowDatabase
) : StudentRepository {

    override fun getAllActiveStudents(): List<StudentData> {
        return database.studentQueries.getAllActiveStudents().executeAsList().map { row ->
            StudentData(
                id = row.id,
                userId = row.user_id,
                firstName = row.first_name,
                lastName = row.last_name,
                phone = row.phone,
                studentId = row.student_id,
                major = row.major,
                yearLevel = row.year_level,
                gpa = row.gpa?.toDouble(),
                email = row.email,
                userType = row.user_type,
                isActive = row.is_active
            )
        }
    }

    override fun getStudentById(id: Long): StudentData? {
        return database.studentQueries.getStudentById(id).executeAsOneOrNull()?.let { row ->
            StudentData(
                id = row.id,
                userId = row.user_id,
                firstName = row.first_name,
                lastName = row.last_name,
                phone = row.phone,
                studentId = row.student_id,
                major = row.major,
                yearLevel = row.year_level,
                gpa = row.gpa?.toDouble(),
                email = row.email,
                userType = row.user_type,
                isActive = row.is_active
            )
        }
    }

    override fun getStudentByUserId(userId: Long): StudentData? {
        return database.studentQueries.getStudentByUserId(userId).executeAsOneOrNull()?.let { row ->
            StudentData(
                id = row.id,
                userId = row.user_id,
                firstName = row.first_name,
                lastName = row.last_name,
                phone = row.phone,
                studentId = row.student_id,
                major = row.major,
                yearLevel = row.year_level,
                gpa = row.gpa?.toDouble(),
                email = row.email,
                userType = row.user_type,
                isActive = row.is_active
            )
        }
    }

    override fun createStudent(
        userId: Long,
        firstName: String,
        lastName: String,
        phone: String,
        studentId: String?,
        major: String?,
        yearLevel: Int?,
        gpa: Double?
    ): Boolean {
        return try {
            database.studentQueries.createStudent(
                user_id = userId,
                first_name = firstName,
                last_name = lastName,
                phone = phone,
                student_id = studentId,
                major = major,
                year_level = yearLevel,
                gpa = gpa?.toBigDecimal()
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun updateStudent(
        id: Long,
        firstName: String,
        lastName: String,
        phone: String,
        studentId: String?,
        major: String?,
        yearLevel: Int?,
        gpa: Double?
    ): Boolean {
        return try {
            database.studentQueries.updateStudent(
                first_name = firstName,
                last_name = lastName,
                phone = phone,
                student_id = studentId,
                major = major,
                year_level = yearLevel,
                gpa = gpa?.toBigDecimal(),
                id = id
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}