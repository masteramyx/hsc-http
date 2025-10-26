package com.shadowconnect.db.models

import java.time.LocalDateTime

data class ContactSubmission(
    val id: Long,
    val name: String,
    val email: String,
    val userType: String,
    val message: String,
    val createdAt: LocalDateTime?
)
