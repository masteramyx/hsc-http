package com.shadowconnect.db

import com.healthshadow.db.HealthShadowDatabase
import com.healthshadow.db.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
}