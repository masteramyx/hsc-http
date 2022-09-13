package com.shadowconnect.model

import kotlinx.serialization.SerialName

data class Professional(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("email")
    var email: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("org_id")
    val orgId: Int?
)