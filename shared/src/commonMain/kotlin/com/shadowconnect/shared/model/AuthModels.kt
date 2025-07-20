package com.shadowconnect.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class LoginResponse(val success: Boolean, val message: String, val user: UserInfo? = null)

@Serializable
data class UserInfo(val id: Long, val email: String, val userType: String)

@Serializable
data class UserSession(
    val userId: Long,
    val email: String,
    val userType: String
)