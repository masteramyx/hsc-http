package com.shadowconnect.db

import com.healthshadow.db.HealthShadowDatabase
import com.shadowconnect.db.mappers.toDomain
import com.shadowconnect.db.models.ContactSubmission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactSubmissionRepositoryImpl(private val database: HealthShadowDatabase) : ContactSubmissionRepository {

    override suspend fun insertContactSubmission(name: String, email: String, userType: String, message: String): Long = withContext(Dispatchers.IO) {
        database.contactSubmissionQueries.insertContactSubmission(name, email, userType, message).executeAsOne()
    }

    override suspend fun getAllContactSubmissions(): List<ContactSubmission> = withContext(Dispatchers.IO) {
        database.contactSubmissionQueries.getAllContactSubmissions()
            .executeAsList()
            .map { it.toDomain() }
    }

    override suspend fun getContactSubmissionById(id: Long): ContactSubmission? = withContext(Dispatchers.IO) {
        database.contactSubmissionQueries.getContactSubmissionById(id)
            .executeAsOneOrNull()
            ?.toDomain()
    }

    override suspend fun getContactSubmissionsByEmail(email: String): List<ContactSubmission> = withContext(Dispatchers.IO) {
        database.contactSubmissionQueries.getContactSubmissionsByEmail(email)
            .executeAsList()
            .map { it.toDomain() }
    }
}
