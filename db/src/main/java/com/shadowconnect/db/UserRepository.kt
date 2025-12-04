package com.shadowconnect.db

import com.healthshadow.db.Users
import java.time.LocalDateTime

interface UserRepository {
    suspend fun findByEmail(email: String): Users?
    suspend fun findById(id: Long): Users?
    suspend fun create(email: String, passwordHash: String, userType: String): Long
    suspend fun emailExists(email: String): Boolean
    suspend fun markEmailVerified(userId: Long)
    suspend fun getUnverifiedUsersOlderThan(cutoffTimestamp: LocalDateTime): List<Users>
    suspend fun getUserFirstName(userId: Long): String?
}