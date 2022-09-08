package com.shadowconnect.model

// todo json serialization
data class Professional(
    val id: Int? = null,
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String,
    val org_id: Int?
) {
}