package com.shadowconnect.db

data class StudentData(
    val id: Long,
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val studentId: String?,
    val major: String?,
    val yearLevel: Int?,
    val gpa: Double?,
    val email: String,
    val userType: String,
    val isActive: Boolean?
)

interface StudentRepository {
    fun getAllActiveStudents(): List<StudentData>
    fun getStudentById(id: Long): StudentData?
    fun getStudentByUserId(userId: Long): StudentData?
    fun createStudent(
        userId: Long,
        firstName: String,
        lastName: String,
        phone: String,
        studentId: String? = null,
        major: String? = null,
        yearLevel: Int? = null,
        gpa: Double? = null
    ): Boolean
    fun updateStudent(
        id: Long,
        firstName: String,
        lastName: String,
        phone: String,
        studentId: String? = null,
        major: String? = null,
        yearLevel: Int? = null,
        gpa: Double? = null
    ): Boolean
}