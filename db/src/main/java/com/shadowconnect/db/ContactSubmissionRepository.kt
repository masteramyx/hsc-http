package com.shadowconnect.db

import com.shadowconnect.db.models.ContactSubmission

interface ContactSubmissionRepository {
    suspend fun insertContactSubmission(name: String, email: String, userType: String, message: String): Long
    suspend fun getAllContactSubmissions(): List<ContactSubmission>
    suspend fun getContactSubmissionById(id: Long): ContactSubmission?
    suspend fun getContactSubmissionsByEmail(email: String): List<ContactSubmission>
}
