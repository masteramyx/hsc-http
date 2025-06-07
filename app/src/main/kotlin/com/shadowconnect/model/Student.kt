package com.shadowconnect.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Student(
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
    @SerialName("student_id")
    val studentId: String? = null,
    @SerialName("major")
    val major: String? = null,
    @SerialName("year_level")
    val yearLevel: Int? = null,
    @SerialName("gpa")
    val gpa: Double? = null,
    @SerialName("user_type")
    val userType: String,
    @SerialName("is_active")
    val isActive: Boolean
)

@Serializable
data class CreateStudentRequest(
    @SerialName("user_id")
    val userId: Long,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("student_id")
    val studentId: String? = null,
    @SerialName("major")
    val major: String? = null,
    @SerialName("year_level")
    val yearLevel: Int? = null,
    @SerialName("gpa")
    val gpa: Double? = null
)