package com.shadowconnect.model

import kotlinx.serialization.Serializable

// todo json serialization
@Serializable
data class Organization(
    val id: Int? = null,
    val name: String,
    val address: String,
    val email: String,
    val phone: String,
    val website: String
) {
}