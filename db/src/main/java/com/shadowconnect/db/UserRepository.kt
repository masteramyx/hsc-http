package com.shadowconnect.db

import com.healthshadow.db.Users

interface UserRepository {
    suspend fun findByEmail(email: String): Users?
    suspend fun findById(id: Long): Users?
    suspend fun create(email: String, passwordHash: String, userType: String): Long
    suspend fun emailExists(email: String): Boolean
}