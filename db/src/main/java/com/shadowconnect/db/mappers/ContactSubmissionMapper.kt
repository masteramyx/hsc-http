package com.shadowconnect.db.mappers

import com.healthshadow.db.Contact_submission
import com.shadowconnect.db.models.ContactSubmission

fun Contact_submission.toDomain(): ContactSubmission {
    return ContactSubmission(
        id = id,
        name = name,
        email = email,
        userType = user_type,
        message = message,
        createdAt = created_at
    )
}
