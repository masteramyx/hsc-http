package com.shadowconnect.model

import kotlinx.serialization.SerialName

data class Professional(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("first_name")
    val first_name: String,
    @SerialName("last_name")
    val last_name: String,
    @SerialName("email")
    val email: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("org_id")
    val org_id: Int?
) {
}