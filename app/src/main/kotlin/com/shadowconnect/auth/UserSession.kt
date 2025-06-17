package com.shadowconnect.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val userId: Long,
    val email: String,
    val userType: String
)