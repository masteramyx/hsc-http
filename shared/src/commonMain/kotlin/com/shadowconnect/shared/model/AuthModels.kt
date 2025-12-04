@file:JsExport
@file:OptIn(ExperimentalJsExport::class)

package com.shadowconnect.shared.model

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.jvm.JvmStatic

@Serializable
enum class UserType {
    STUDENT,
    PROFESSIONAL;

    companion object {
        @JvmStatic
        fun fromString(name: String): UserType {
            return entries.first { type -> type.name.lowercase() == name.lowercase() }
        }
    }
}

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class LoginResponse(val success: Boolean, val message: String, val user: UserInfo? = null)

@Serializable
data class LogoutResponse(val success: Boolean, val message: String)

@Serializable
data class UserInfo(
    val id: Long,
    val email: String,
    val userType: UserType,
    val emailVerified: Boolean = false
)

@Serializable
data class SessionState(
    val isLoggedIn: Boolean = false,
    val user: UserInfo? = null,
    val isLoading: Boolean = false
)