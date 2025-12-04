package com.shadowconnect.db

import com.healthshadow.db.HealthShadowDatabase
import com.healthshadow.db.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class UserRepositoryImpl(private val database: HealthShadowDatabase) : UserRepository {

    override suspend fun findByEmail(email: String): Users? = withContext(Dispatchers.IO) {
        database.userQueries.getUserByEmail(email).executeAsOneOrNull()
    }

    override suspend fun findById(id: Long): Users? = withContext(Dispatchers.IO) {
        database.userQueries.getUserById(id).executeAsOneOrNull()
    }

    override suspend fun create(email: String, passwordHash: String, userType: String): Long = withContext(Dispatchers.IO) {
        database.userQueries.createUser(email, passwordHash, userType).executeAsOne()
    }

    override suspend fun emailExists(email: String): Boolean = withContext(Dispatchers.IO) {
        database.userQueries.isEmailAlreadyExists(email).executeAsOneOrNull() != null
    }

    override suspend fun markEmailVerified(userId: Long) = withContext(Dispatchers.IO) {
        database.userQueries.markEmailVerified(userId)
    }

    override suspend fun getUnverifiedUsersOlderThan(cutoffTimestamp: LocalDateTime): List<Users> =
        withContext(Dispatchers.IO) {
            database.userQueries.getUnverifiedUsersOlderThan(cutoffTimestamp).executeAsList()
        }

    override suspend fun getUserFirstName(userId: Long): String? =
        withContext(Dispatchers.IO) {
            database.userQueries.getUserFirstName(userId).executeAsOneOrNull()?.firstName
        }
}