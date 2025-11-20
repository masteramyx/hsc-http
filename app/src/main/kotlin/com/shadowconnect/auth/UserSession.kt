package com.shadowconnect.auth

import com.shadowconnect.shared.model.UserType
import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val userId: Long,
    val email: String,
    val userType: UserType
) : Principal